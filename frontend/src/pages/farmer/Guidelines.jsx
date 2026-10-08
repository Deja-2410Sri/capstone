import { useState, useEffect } from 'react';
import { guidelineAPI } from '../../services/api';

export default function FarmerGuidelines() {
  const [guidelines, setGuidelines] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { guidelineAPI.getAll().then(res => setGuidelines(res.data.data || [])).catch(console.error).finally(() => setLoading(false)); }, []);

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;

  return (
    <div className="container py-4">
      <h2 className="mb-4">Farming Guidelines</h2>
      {guidelines.length === 0 ? <div className="alert alert-info">No guidelines available yet.</div> :
        <div className="row g-4">{guidelines.map(g => (
          <div key={g.id} className="col-md-6">
            <div className="card h-100 shadow-sm">
              <div className="card-header bg-success text-white"><h5 className="mb-0">{g.cropName}</h5></div>
              <div className="card-body">
                {g.sowingMethod && <div className="mb-2"><strong>Sowing:</strong> <span className="small">{g.sowingMethod}</span></div>}
                {g.irrigation && <div className="mb-2"><strong>Irrigation:</strong> <span className="small">{g.irrigation}</span></div>}
                {g.fertilizer && <div className="mb-2"><strong>Fertilizer:</strong> <span className="small">{g.fertilizer}</span></div>}
                {g.pestManagement && <div className="mb-2"><strong>Pest Management:</strong> <span className="small">{g.pestManagement}</span></div>}
                {g.harvesting && <div className="mb-2"><strong>Harvesting:</strong> <span className="small">{g.harvesting}</span></div>}
                {g.generalTips && <div className="mb-2"><strong>Tips:</strong> <span className="small">{g.generalTips}</span></div>}
              </div>
            </div>
          </div>
        ))}</div>
      }
    </div>
  );
}
