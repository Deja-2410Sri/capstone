import { useState, useEffect } from 'react';
import { cropAPI } from '../../services/api';

export default function AdminCrops() {
  const [crops, setCrops] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({ cropName: '', category: '', season: '', soilType: '', waterRequirement: '', description: '' });

  useEffect(() => { loadCrops(); }, []);
  const loadCrops = async () => { try { const res = await cropAPI.search({ page: 0, size: 50 }); setCrops(res.data.data?.content || []); } catch (err) { console.error(err); } finally { setLoading(false); } };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try { await cropAPI.create(formData); setShowForm(false); setFormData({ cropName: '', category: '', season: '', soilType: '', waterRequirement: '', description: '' }); loadCrops(); }
    catch (err) { alert(err.response?.data?.message || 'Error'); }
  };

  const handleDelete = async (id) => { if (!confirm('Delete?')) return; try { await cropAPI.delete(id); loadCrops(); } catch (err) { alert('Error'); } };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;
  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Crop Management</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(true)}>+ Add Crop</button>
      </div>
      {showForm && (
        <div className="card mb-4"><div className="card-body">
          <form onSubmit={handleSubmit} className="row g-3">
            {['cropName', 'category', 'season', 'soilType', 'waterRequirement', 'description'].map(f => (
              <div key={f} className="col-md-4"><label className="form-label">{f.replace(/([A-Z])/g, ' $1')}</label>
                <input className="form-control" required={f === 'cropName'} value={formData[f]} onChange={e => setFormData({...formData, [f]: e.target.value})} /></div>
            ))}
            <div className="col-12"><button type="submit" className="btn btn-success me-2">Save</button><button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button></div>
          </form>
        </div></div>
      )}
      <div className="table-responsive">
        <table className="table table-hover">
          <thead><tr><th>Name</th><th>Category</th><th>Season</th><th>Soil</th><th>Water</th><th>Actions</th></tr></thead>
          <tbody>{crops.map(c => (
            <tr key={c.id}><td>{c.cropName}</td><td>{c.category}</td><td>{c.season}</td><td>{c.soilType}</td><td>{c.waterRequirement}</td>
              <td><button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id)}>Delete</button></td></tr>
          ))}</tbody>
        </table>
      </div>
    </div>
  );
}
