import './App.css';
import MainPage from './pages/main_page'; 
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import * as ROUTES from './constants/routes';
import { useState, lazy, Suspense } from 'react';

const Dashboard = lazy(() => import('./pages/main_page'));
const Specitems = lazy(() => import('./pages/specitems_page'));
const Specitem = lazy(() => import('./pages/specitem_page'));
const Export = lazy(() => import('./pages/export'))

function App() {
  const [exportList, setExportList] = useState([]);

  return (
    <div className="App">
      
      <Router>
        <Suspense fallback={<p>Loading...</p>}>
          <Routes>
            <Route path={ROUTES.DASHBOARD} element={<Dashboard/>} />
            <Route path={ROUTES.SPECITEMS} element={<Specitems exportList={exportList} setExportList={setExportList}/>} />
            <Route path={ROUTES.SPECITEM} element={<Specitem exportList={exportList} setExportList={setExportList}/>} />
            <Route path={ROUTES.EXPORT} element={<Export exportList={exportList} setExportList={setExportList}/>} />
          </Routes>
        </Suspense>
      </Router>  
      <ToastContainer autoClose={3000} hideProgressBar />
    </div>
  );
}

export default App;
