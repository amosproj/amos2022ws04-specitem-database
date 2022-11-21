import './App.css';
import MainPage from './pages/main_page'; 
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import * as ROUTES from './constants/routes';
import { lazy, Suspense } from 'react';

const Dashboard = lazy(() => import('./pages/main_page'));
const Specitems = lazy(() => import('./pages/specitems_page'));
const Specitem = lazy(() => import('./pages/specitem_page'));

function App() {
  return (
    <div className="App">
      
      <Router>
        <Suspense fallback={<p>Loading...</p>}>
          <Routes>
            <Route path={ROUTES.DASHBOARD} element={<Dashboard/>} />
            <Route path={ROUTES.SPECITEMS} element={<Specitems/>} />
            <Route path={ROUTES.SPECITEM} element={<Specitem/>} />
          </Routes>
        </Suspense>
      </Router>  
      <ToastContainer autoClose={3000} hideProgressBar />
    </div>
  );
}

export default App;
