import { useState, useEffect } from 'react';
import { recommendationAPI } from '../../services/api';

export default function FarmerRecommendations() {
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);

  useEffect(() => { loadRecommendations(); }, []);

  const loadRecommendations = async () => {
    try {
      const res = await recommendationAPI.getAll();
      setRecommendations(res.data.data || []);
    } catch (err) { console.error(err); } finally { setLoading(false); }
  };

  const handleGenerate = async () => {
    setGenerating(true);
    try {
      const res = await recommendationAPI.generate();
      setRecommendations(prev => [...(res.data.data || []), ...prev]);
    } catch (err) { alert(err.response?.data?.message || 'Error generating recommendations'); }
    finally { setGenerating(false); }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Crop Recommendations</h2>
        <button className="btn btn-success" onClick={handleGenerate} disabled={generating}>
          {generating ? 'Generating...' : 'Generate Recommendations'}
        </button>
      </div>
      {recommendations.length === 0 ? (
        <div className="alert alert-info">No recommendations yet. Add a farm and click "Generate Recommendations".</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover">
            <thead><tr><th>Crop</th><th>Season</th><th>Soil Type</th><th>Suitability</th><th>Reason</th><th>Status</th></tr></thead>
            <tbody>{recommendations.map(rec => (
              <tr key={rec.id}>
                <td><strong>{rec.cropName}</strong></td><td>{rec.season}</td><td>{rec.soilType}</td>
                <td><span className="badge bg-info">{rec.suitabilityScore?.toFixed(0)}%</span></td>
                <td className="small">{rec.reason}</td>
                <td><span className={`badge bg-${rec.status === 'GENERATED' ? 'secondary' : rec.status === 'ACCEPTED' ? 'success' : 'primary'}`}>{rec.status}</span></td>
              </tr>
            ))}</tbody>
          </table>
        </div>
      )}
    </div>
  );
}
