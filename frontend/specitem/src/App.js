import './App.css';
import MainPage from './pages/main_page'; 
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import * as ROUTES from './constants/routes';
import { useState, lazy, Suspense } from 'react';
import Context from './context/Context';

const Dashboard = lazy(() => import('./pages/main_page'));
const Specitems = lazy(() => import('./pages/specitems_page'));
const Specitem = lazy(() => import('./pages/specitem_page'));
const Export = lazy(() => import('./pages/export'))
const SpecitemHistory = lazy(() => import('./pages/specitem_history'))

function App() {
  const [exportList, setExportList] = useState([]);
  const value = {exportList, setExportList};

  return (
    <div className="App">
      <Context.Provider value={value}>
        <Router>
          <Suspense fallback={<p>Loading...</p>}>
            <Routes>
              <Route path={ROUTES.DASHBOARD} element={<Dashboard/>} />
              <Route path={ROUTES.SPECITEMS} element={<Specitems/>} />
              <Route path={ROUTES.SPECITEM} element={<Specitem/>} />
              <Route path={ROUTES.EXPORT} element={<Export/>} />
              <Route path={ROUTES.SPECITEM_HISTORY} element={<SpecitemHistory/>} />
            </Routes>
          </Suspense>
        </Router> 
      </Context.Provider> 
      <ToastContainer autoClose={3000} hideProgressBar />
    </div>
  );
}

export default App;
