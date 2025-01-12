import os
import torch
from torchvision import transforms
from PIL import Image
from model_def import PeopleCounter

# Naloži model
model = PeopleCounter()
model.load_state_dict(torch.load('people_counter.pth'))
model.eval()

# Preveri napravo (CPU ali GPU)
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model.to(device)

# Transformacija slike
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# Pot do testnih slik
test_folder = 'test_data/test'

# Napovedi za vse slike v testni mapi
for image_name in os.listdir(test_folder):
    image_path = os.path.join(test_folder, image_name)

    if not image_name.lower().endswith(('.png', '.jpg', '.jpeg')):
        continue

    print(f"Obdelujem sliko: {image_name}")  # Izpis, da vidiš katero sliko obdeluješ

    image = Image.open(image_path)

    # Preveri, če slika ni RGB in jo pretvori
    if image.mode != 'RGB':
        image = image.convert('RGB')

    image = transform(image).unsqueeze(0).to(device)

    # Končni izpisi
    with torch.no_grad():
        prediction = model(image).item() 
        print(f"Napoved (nezaokroženo): {prediction}")  

        predicted_people = round(prediction) 
        print(f"Napovedano število ljudi (zaokroženo): {predicted_people}")