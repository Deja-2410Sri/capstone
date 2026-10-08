import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => { logout(); navigate('/login'); };

  if (!user) return null;

  const farmerLinks = [
    { to: '/dashboard', label: 'Dashboard' },
    { to: '/farms', label: 'Farms' },
    { to: '/crops', label: 'Crops' },
    { to: '/recommendations', label: 'Recommendations' },
    { to: '/guidelines', label: 'Guidelines' },
    { to: '/diseases', label: 'Diseases' },
    { to: '/expert-advice', label: 'Expert Advice' },
    { to: '/weather', label: 'Weather' },
    { to: '/market-prices', label: 'Market' },
    { to: '/notifications', label: 'Notifications' },
  ];

  const expertLinks = [
    { to: '/expert/dashboard', label: 'Dashboard' },
    { to: '/expert/questions', label: 'Questions' },
    { to: '/guidelines', label: 'Guidelines' },
    { to: '/diseases', label: 'Diseases' },
  ];

  const adminLinks = [
    { to: '/admin/dashboard', label: 'Dashboard' },
    { to: '/admin/users', label: 'Users' },
    { to: '/admin/crops', label: 'Crops' },
  ];

  const links = user.role === 'ROLE_ADMIN' ? adminLinks : user.role === 'ROLE_EXPERT' ? expertLinks : farmerLinks;

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-success">
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">Crop Advisory</Link>
        <div className="collapse navbar-collapse">
          <ul className="navbar-nav me-auto">
            {links.map((link) => (
              <li key={link.to} className="nav-item">
                <Link className="nav-link" to={link.to}>{link.label}</Link>
              </li>
            ))}
          </ul>
          <div className="d-flex align-items-center">
            <span className="text-light me-3">{user.firstName} ({user.role.replace('ROLE_', '')})</span>
            <Link to="/profile" className="btn btn-outline-light btn-sm me-2">Profile</Link>
            <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>Logout</button>
          </div>
        </div>
      </div>
    </nav>
  );
}
