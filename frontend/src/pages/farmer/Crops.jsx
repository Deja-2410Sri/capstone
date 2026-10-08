import { useState, useEffect } from 'react';
import { cropAPI } from '../../services/api';

export default function FarmerCrops() {
  const [crops, setCrops] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState({ name: '', season: '', soilType: '' });

  useEffect(() => { loadCrops(); }, []);

  const loadCrops = async (filters = {}) => {
    setLoading(true);
    try {
      const res = await cropAPI.search({ ...filters, page: 0, size: 50 });
      setCrops(res.data.data.content || []);
    } catch (err) { console.error(err); } finally { setLoading(false); }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    loadCrops(search);
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;

  return (
    <div className="container py-4">
      <h2 className="mb-4">Crops</h2>
      <form onSubmit={handleSearch} className="row g-3 mb-4">
        <div className="col-md-3">
          <input className="form-control" placeholder="Search by name" value={search.name} onChange={e => setSearch({...search, name: e.target.value})} />
        </div>
        <div className="col-md-3">
          <select className="form-select" value={search.season} onChange={e => setSearch({...search, season: e.target.value})}>
            <option value="">All Seasons</option>
            <option value="Kharif">Kharif</option>
            <option value="Rabi">Rabi</option>
            <option value="Both">Both</option>
          </select>
        </div>
        <div className="col-md-3">
          <select className="form-select" value={search.soilType} onChange={e => setSearch({...search, soilType: e.target.value})}>
            <option value="">All Soil Types</option>
            <option value="Clay">Clay</option>
            <option value="Loamy">Loamy</option>
            <option value="Sandy">Sandy</option>
            <option value="Black">Black</option>
          </select>
        </div>
        <div className="col-md-3">
          <button type="submit" className="btn btn-primary w-100">Search</button>
        </div>
      </form>
      <div className="row g-4">
        {crops.map(crop => (
          <div key={crop.id} className="col-md-4">
            <div className="card h-100 shadow-sm">
              <div className="card-body">
                <h5 className="card-title">{crop.cropName}</h5>
                <p className="text-muted mb-2">{crop.category} | {crop.season}</p>
                <p className="card-text small">{crop.description}</p>
                <div><span className="badge bg-info me-1">Soil: {crop.soilType}</span>
                <span className="badge bg-primary">Water: {crop.waterRequirement}</span></div>
              </div>
            </div>
          </div>
        ))}
        {crops.length === 0 && <div className="col-12"><div className="alert alert-info">No crops found.</div></div>}
      </div>
    </div>
  );
}
