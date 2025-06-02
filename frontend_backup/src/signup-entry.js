// public/signup-entry.js
import React from "react";
import ReactDOM from "react-dom/client";
import RegisterForm from "./components/RegisterForm"; // 상대 경로가 아닌 빌드 후 경로로 접근될 수 있도록 설정 필요

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<RegisterForm />);
