import os
import torch
from torch.utils.data import Dataset, DataLoader
from torchvision import transforms
from PIL import Image
import random

# Definicija dataset razreda
class PeopleCounterDataset(Dataset):
    def __init__(self, image_folder, transform=None, repetitions=30):
        self.image_folder = image_folder
        self.transform = transform
        self.repetitions = repetitions
        self.image_paths = []
        
        # Dodajanje vseh slik v seznam
        for root, dirs, files in os.walk(image_folder):
            for file in files:
                if file.lower().endswith(('.png', '.jpg', '.jpeg')):
                    self.image_paths.append(os.path.join(root, file))

    def __len__(self):
        # Vsako sliko uporabimo `repetitions`-krat, da povečamo število podatkov za učenje
        return len(self.image_paths) * self.repetitions

    def __getitem__(self, idx):
        # Izračunaj indeks slike in koliko ponovitev te slike potrebujemo
        image_idx = idx // self.repetitions
        image_path = self.image_paths[image_idx]
        image = Image.open(image_path)
        
        # Preverimo ali je slika v RGB formatu in jo po potrebi pretvorimo
        if image.mode != 'RGB':
            image = image.convert('RGB')
        
        # Naključno izberemo transformacijo
        if self.transform:
            image = self.transform(image)
        
        # Pridobimo število ljudi iz imena podmape
        folder_name = os.path.basename(os.path.dirname(image_path))
        target = int(folder_name)
        
        return image, target

# Naključne transformacije za slike
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.RandomRotation(20),  # Naključna rotacija
    transforms.RandomHorizontalFlip(),  # Naključni horizontalni flip
    transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2, hue=0.2),  # Spreminjanje svetlosti in kontrasta
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# Pot do podatkov
train_data_folder = 'train_data'

# Ustvarimo dataset in DataLoader
dataset = PeopleCounterDataset(train_data_folder, transform=transform, repetitions=30)
train_loader = DataLoader(dataset, batch_size=32, shuffle=True)

# Import modela iz zunanje datoteke
from model_def import PeopleCounter

# Inicializacija modela in optimizatorja
model = PeopleCounter().cuda()  # Preverite, ali imate GPU
optimizer = torch.optim.Adam(model.parameters(), lr=0.0001, weight_decay=1e-4) 
criterion = torch.nn.SmoothL1Loss()

# Trening modela
if __name__ == "__main__":
    num_epochs = 40
    for epoch in range(num_epochs):
        model.train()
        running_loss = 0.0
        for i, (inputs, labels) in enumerate(train_loader):
            inputs, labels = inputs.cuda(), labels.cuda()

            optimizer.zero_grad()

            outputs = model(inputs)

            # Izračun izgube 
            loss = criterion(outputs, labels.unsqueeze(1).float())
            loss.backward()

            # Posodabljanje uteži
            optimizer.step()

            running_loss += loss.item()

        print(f"Epoch {epoch+1}/{num_epochs}, Loss: {running_loss/len(train_loader)}")

    # Shranimo model
    torch.save(model.state_dict(), 'people_counter.pth')