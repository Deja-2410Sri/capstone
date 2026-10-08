import { useState } from 'react';
import { weatherAPI } from '../../services/api';

export default function FarmerWeather() {
  const [location, setLocation] = useState('');
  const [weather, setWeather] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault(); setLoading(true);
    try { const res = await weatherAPI.getByLocation(location); setWeather(res.data.data); }
    catch (err) { console.error(err); } finally { setLoading(false); }
  };

  return (
    <div className="container py-4">
      <h2 className="mb-4">Weather Information</h2>
      <form onSubmit={handleSearch} className="row g-3 mb-4">
        <div className="col-md-6"><input className="form-control" placeholder="Enter location" value={location} onChange={e => setLocation(e.target.value)} required /></div>
        <div className="col-md-3"><button type="submit" className="btn btn-primary w-100" disabled={loading}>{loading ? 'Loading...' : 'Get Weather'}</button></div>
      </form>
      {weather && (
        <div className="card"><div className="card-body">
          <h5>{weather.location}</h5>
          <div className="row">
            <div className="col-md-3"><strong>Temperature:</strong> {weather.temperature || 'N/A'}C</div>
            <div className="col-md-3"><strong>Humidity:</strong> {weather.humidity || 'N/A'}%</div>
            <div className="col-md-3"><strong>Rainfall:</strong> {weather.rainfall || 'N/A'}mm</div>
            <div className="col-md-3"><strong>Condition:</strong> {weather.weatherCondition || 'N/A'}</div>
          </div>
        </div></div>
      )}
    </div>
  );
}
