import { useState, useEffect } from 'react';
import { profileAPI } from '../../services/api';

export default function FarmerProfile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({});

  useEffect(() => { profileAPI.get().then(res => { setProfile(res.data.data); setFormData(res.data.data); }).catch(console.error).finally(() => setLoading(false)); }, []);

  const handleSave = async () => {
    try { const res = await profileAPI.update(formData); setProfile(res.data.data); setEditing(false); }
    catch (err) { alert(err.response?.data?.message || 'Error updating profile'); }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;
  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>My Profile</h2>
        <button className="btn btn-primary" onClick={() => editing ? handleSave() : setEditing(true)}>
          {editing ? 'Save' : 'Edit'}
        </button>
      </div>
      {profile && (
        <div className="card"><div className="card-body">
          <div className="row g-3">
            {[['firstName', 'First Name'], ['lastName', 'Last Name'], ['email', 'Email'], ['phone', 'Phone'],
              ['location', 'Location'], ['address', 'Address'], ['district', 'District'], ['state', 'State'],
              ['postalCode', 'Postal Code'], ['soilType', 'Soil Type'], ['farmSize', 'Farm Size']].map(([key, label]) => (
              <div key={key} className="col-md-6">
                <label className="form-label">{label}</label>
                <input className="form-control" value={formData[key] || ''} disabled={!editing || key === 'email'}
                  onChange={e => setFormData({...formData, [key]: e.target.value})} />
              </div>
            ))}
          </div>
        </div></div>
      )}
    </div>
  );
}
