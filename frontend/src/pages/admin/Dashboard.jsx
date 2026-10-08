import { useState, useEffect } from 'react';
import { adminAPI } from '../../services/api';

export default function AdminDashboard() {
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => { adminAPI.getDashboard().then(res => setStats(res.data.data || {})).catch(console.error).finally(() => setLoading(false)); }, []);

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;

  const cards = [
    { title: 'Total Users', value: stats.totalUsers, color: 'primary' },
    { title: 'Total Crops', value: stats.totalCrops, color: 'success' },
    { title: 'Recommendations', value: stats.totalRecommendations, color: 'info' },
    { title: 'Open Questions', value: stats.openExpertQuestions, color: 'warning' },
    { title: 'Disease Records', value: stats.diseasePestRecords, color: 'danger' },
  ];

  return (
    <div className="container py-4">
      <h2 className="mb-4">Admin Dashboard</h2>
      <div className="row g-4">
        {cards.map(c => (
          <div key={c.title} className="col-md-4 col-lg-3">
            <div className={`card border-${c.color} shadow-sm`}>
              <div className="card-body text-center">
                <h5 className="text-muted">{c.title}</h5>
                <h2 className={`text-${c.color}`}>{c.value || 0}</h2>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
