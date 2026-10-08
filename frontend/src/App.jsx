import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/public/Login';
import Register from './pages/public/Register';
import FarmerDashboard from './pages/farmer/Dashboard';
import FarmerFarms from './pages/farmer/Farms';
import FarmerCrops from './pages/farmer/Crops';
import FarmerRecommendations from './pages/farmer/Recommendations';
import FarmerGuidelines from './pages/farmer/Guidelines';
import FarmerDiseases from './pages/farmer/Diseases';
import FarmerAdvice from './pages/farmer/ExpertAdvice';
import FarmerWeather from './pages/farmer/Weather';
import FarmerMarketPrices from './pages/farmer/MarketPrices';
import FarmerNotifications from './pages/farmer/Notifications';
import FarmerProfile from './pages/farmer/Profile';
import ExpertDashboard from './pages/expert/Dashboard';
import ExpertQuestions from './pages/expert/Questions';
import AdminDashboard from './pages/admin/Dashboard';
import AdminUsers from './pages/admin/Users';
import AdminCrops from './pages/admin/Crops';
import Navbar from './components/Navbar';

function AppRoutes() {
  const { user } = useAuth();

  const getHomeRoute = () => {
    if (!user) return '/login';
    if (user.role === 'ROLE_ADMIN') return '/admin/dashboard';
    if (user.role === 'ROLE_EXPERT') return '/expert/dashboard';
    return '/dashboard';
  };

  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Farmer Routes */}
        <Route path="/dashboard" element={<ProtectedRoute allowedRoles={['ROLE_FARMER']}><FarmerDashboard /></ProtectedRoute>} />
        <Route path="/farms" element={<ProtectedRoute allowedRoles={['ROLE_FARMER']}><FarmerFarms /></ProtectedRoute>} />
        <Route path="/crops" element={<ProtectedRoute allowedRoles={['ROLE_FARMER', 'ROLE_EXPERT', 'ROLE_ADMIN']}><FarmerCrops /></ProtectedRoute>} />
        <Route path="/recommendations" element={<ProtectedRoute allowedRoles={['ROLE_FARMER']}><FarmerRecommendations /></ProtectedRoute>} />
        <Route path="/guidelines" element={<ProtectedRoute allowedRoles={['ROLE_FARMER', 'ROLE_EXPERT', 'ROLE_ADMIN']}><FarmerGuidelines /></ProtectedRoute>} />
        <Route path="/diseases" element={<ProtectedRoute allowedRoles={['ROLE_FARMER', 'ROLE_EXPERT', 'ROLE_ADMIN']}><FarmerDiseases /></ProtectedRoute>} />
        <Route path="/expert-advice" element={<ProtectedRoute allowedRoles={['ROLE_FARMER']}><FarmerAdvice /></ProtectedRoute>} />
        <Route path="/weather" element={<ProtectedRoute allowedRoles={['ROLE_FARMER']}><FarmerWeather /></ProtectedRoute>} />
        <Route path="/market-prices" element={<ProtectedRoute allowedRoles={['ROLE_FARMER']}><FarmerMarketPrices /></ProtectedRoute>} />
        <Route path="/notifications" element={<ProtectedRoute><FarmerNotifications /></ProtectedRoute>} />
        <Route path="/profile" element={<ProtectedRoute><FarmerProfile /></ProtectedRoute>} />

        {/* Expert Routes */}
        <Route path="/expert/dashboard" element={<ProtectedRoute allowedRoles={['ROLE_EXPERT']}><ExpertDashboard /></ProtectedRoute>} />
        <Route path="/expert/questions" element={<ProtectedRoute allowedRoles={['ROLE_EXPERT']}><ExpertQuestions /></ProtectedRoute>} />

        {/* Admin Routes */}
        <Route path="/admin/dashboard" element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']}><AdminDashboard /></ProtectedRoute>} />
        <Route path="/admin/users" element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']}><AdminUsers /></ProtectedRoute>} />
        <Route path="/admin/crops" element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']}><AdminCrops /></ProtectedRoute>} />

        <Route path="*" element={<Navigate to={getHomeRoute()} replace />} />
      </Routes>
    </>
  );
}

export default function App() {
  return (
    <Router>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  );
}
