import { useState, useEffect } from 'react';
import { diseaseAPI } from '../../services/api';

export default function FarmerDiseases() {
  const [diseases, setDiseases] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { diseaseAPI.getAll().then(res => setDiseases(res.data.data || [])).catch(console.error).finally(() => setLoading(false)); }, []);

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;

  return (
    <div className="container py-4">
      <h2 className="mb-4">Disease & Pest Information</h2>
      {diseases.length === 0 ? <div className="alert alert-info">No disease records available yet.</div> :
        <div className="row g-4">{diseases.map(d => (
          <div key={d.id} className="col-md-6">
            <div className="card h-100 shadow-sm border-warning">
              <div className="card-header bg-warning"><h5 className="mb-0">{d.diseaseName} <small className="text-muted">({d.cropName})</small></h5></div>
              <div className="card-body">
                {d.symptoms && <div className="mb-2"><strong>Symptoms:</strong> <span className="small">{d.symptoms}</span></div>}
                {d.causes && <div className="mb-2"><strong>Causes:</strong> <span className="small">{d.causes}</span></div>}
                {d.prevention && <div className="mb-2"><strong>Prevention:</strong> <span className="small">{d.prevention}</span></div>}
                {d.treatment && <div className="mb-2"><strong>Treatment:</strong> <span className="small">{d.treatment}</span></div>}
              </div>
            </div>
          </div>
        ))}</div>
      }
    </div>
  );
}
