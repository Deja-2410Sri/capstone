import { useState, useEffect } from 'react';
import { adviceAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

export default function ExpertDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ questions: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    adviceAPI.getAll({ page: 0, size: 1 }).then(res => {
      setStats({ questions: res.data.data?.totalElements || 0 });
    }).catch(console.error).finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;
  return (
    <div className="container py-4">
      <h2>Welcome, Dr. {user?.lastName}!</h2>
      <div className="row g-4 mt-3">
        <div className="col-md-4"><div className="card border-primary shadow-sm"><div className="card-body"><h5>Total Questions</h5><h2>{stats.questions}</h2></div></div></div>
      </div>
    </div>
  );
}
