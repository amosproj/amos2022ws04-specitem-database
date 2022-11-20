import './App.css';
import MainPage from './pages/main_page'; 
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function App() {
  return (
    <div className="App">
      <MainPage/>
      <ToastContainer autoClose={3000} hideProgressBar />
    </div>
  );
}

export default App;
