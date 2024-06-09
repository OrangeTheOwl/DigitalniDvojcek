import { Container } from "@mui/material";
import React, { useEffect, useState } from "react";
import Header from "../components/Header";

// Chart
import { Bar, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const Graph = () => {
  const [trafficInfo, setTrafficInfo] = useState([]);
  const [nesrecasCount, setNesrecas] = useState(0);
  const [zastojCount, setZastoj] = useState(0);
  const [brezZastojevCount, setBrezZastojev] = useState(0);
  const [deloNaCestiCount, setDeloNaCesti] = useState(0);

  const chartData = {
    labels: ["Nesreča", "Zastoj", "Brez zastojev", "Delo na cesti"],
    datasets: [
      {
        label: "Število",
        data: [
          nesrecasCount / 2,
          zastojCount / 2,
          brezZastojevCount / 2,
          deloNaCestiCount / 2,
        ],
        backgroundColor: [
          "rgba(255, 99, 132, 0.2)",
          "rgba(54, 162, 235, 0.2)",
          "rgba(255, 206, 86, 0.2)",
          "rgba(75, 192, 192, 0.2)",
        ],
        borderColor: [
          "rgba(255, 99, 132, 1)",
          "rgba(54, 162, 235, 1)",
          "rgba(255, 206, 86, 1)",
          "rgba(75, 192, 192, 1)",
        ],
        borderWidth: 1,
      },
    ],
  };

  const getAllTrafficInfo = async () => {
    try {
      const response = await fetch("http://localhost:3210/trafficInfo");
      const data = await response.json();
      console.log(data);
      setTrafficInfo(data);
      data.forEach((element) => {
        if (element.status === "nesreča") {
          setNesrecas((prev) => prev + 1);
        } else if (element.status === "zastoj") {
          setZastoj((prev) => prev + 1);
        } else if (element.status === "brez zastojev") {
          setBrezZastojev((prev) => prev + 1);
        } else if (element.status === "delo na cesti") {
          setDeloNaCesti((prev) => prev + 1);
        }
      });
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getAllTrafficInfo();
  }, []);

  // useEffect(() => {
  //   console.log(nesrecasCount);
  //   console.log(zastojCount);
  //   console.log(brezZastojevCount);
  //   console.log(deloNaCestiCount);
  // }, [nesrecasCount, zastojCount, brezZastojevCount, deloNaCestiCount]);

  return (
    <Container>
      <Header page="Graph" />
      <Bar options={{}} data={chartData} />
    </Container>
  );
};

export default Graph;
