import { Container } from "@mui/material";
import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import { Bar } from "react-chartjs-2";
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
  const [flightData, setFlightData] = useState({ labels: [], counts: [] });

  const chartData = {
    labels: ["Nesreča", "Zastoj", "Brez zastojev", "Delo na cesti"],
    datasets: [
      {
        label: "Število",
        data: [nesrecasCount, zastojCount, brezZastojevCount, deloNaCestiCount],
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

  const flightChartData = {
    labels: flightData.labels,
    datasets: [
      {
        label: "Število letov",
        data: flightData.counts,
        backgroundColor: "rgba(75, 192, 192, 0.2)",
        borderColor: "rgba(75, 192, 192, 1)",
        borderWidth: 1,
      },
    ],
  };

  const getAllTrafficInfo = async () => {
    setNesrecas(0);
    setZastoj(0);
    setBrezZastojev(0);
    setDeloNaCesti(0);
    try {
      const response = await fetch("http://localhost:3210/trafficInfo");
      const data = await response.json();
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

  const getFlightData = async () => {
    try {
      const response = await fetch("http://localhost:3210/flight");
      const data = await response.json();

      const airportFlightCounts = data.reduce((acc, flight) => {
        acc[flight.airport.name] = (acc[flight.airport] || 0) + 1;
        return acc;
      }, {});

      const labels = Object.keys(airportFlightCounts);
      const counts = Object.values(airportFlightCounts);

      setFlightData({ labels, counts });
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    console.log(flightData);
  }, [flightData]);

  useEffect(() => {
    getAllTrafficInfo();
    getFlightData();
  }, []);

  return (
    <Container>
      <Header page="Graf" />
      <br />
      <Bar options={{}} data={chartData} />
      <br />
      <br />
      <Bar options={{}} data={flightChartData} />
    </Container>
  );
};

export default Graph;
