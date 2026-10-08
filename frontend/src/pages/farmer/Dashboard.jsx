import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { farmAPI, recommendationAPI, notificationAPI } from '../../services/api';

export default function FarmerDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ farms: 0, recommendations: 0, unread: 0 });
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const [farmsRes, recsRes, unreadRes] = await Promise.all([
        farmAPI.getAll({ page: 0, size: 1 }),
        recommendationAPI.getAll(),
        notificationAPI.getUnreadCount(),
      ]);
      setStats({
        farms: farmsRes.data.data.totalElements || 0,
        recommendations: recsRes.data.data?.length || 0,
        unread: unreadRes.data.data || 0,
      });
      setRecommendations(recsRes.data.data?.slice(0, 5) || []);
    } catch (err) {
      console.error('Dashboard load error:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;

  return (
    <div className="container-fluid py-4">
      <h2 className="mb-4">Welcome, {user?.firstName}!</h2>
      <div className="row g-4 mb-4">
        {[
          { title: 'Total Farms', value: stats.farms, link: '/farms', color: 'primary' },
          { title: 'Recommended Crops', value: stats.recommendations, link: '/recommendations', color: 'success' },
          { title: 'Unread Notifications', value: stats.unread, link: '/notifications', color: 'warning' },
        ].map((card) => (
          <div key={card.title} className="col-md-4">
            <div className={`card border-${card.color} shadow-sm`}>
              <div className="card-body">
                <h5 className="card-title text-muted">{card.title}</h5>
                <h2 className={`text-${card.color}`}>{card.value}</h2>
                <Link to={card.link} className="stretched-link" />
              </div>
            </div>
          </div>
        ))}
      </div>
      {recommendations.length > 0 && (
        <div className="card shadow-sm">
          <div className="card-header"><h5>Recent Recommendations</h5></div>
          <div className="card-body p-0">
            <table className="table table-hover mb-0">
              <thead><tr><th>Crop</th><th>Season</th><th>Suitability</th><th>Status</th></tr></thead>
              <tbody>
                {recommendations.map((rec) => (
                  <tr key={rec.id}>
                    <td>{rec.cropName}</td><td>{rec.season}</td>
                    <td><span className="badge bg-info">{rec.suitabilityScore?.toFixed(0)}%</span></td>
                    <td><span className={`badge bg-${rec.status === 'GENERATED' ? 'secondary' : 'success'}`}>{rec.status}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
