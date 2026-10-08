import { useState, useEffect } from 'react';
import { marketPriceAPI } from '../../services/api';

export default function FarmerMarketPrices() {
  const [prices, setPrices] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => { marketPriceAPI.getAll().then(res => setPrices(res.data.data || [])).catch(console.error).finally(() => setLoading(false)); }, []);
  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;
  return (
    <div className="container py-4">
      <h2 className="mb-4">Market Prices</h2>
      {prices.length === 0 ? <div className="alert alert-info">No market prices available.</div> :
        <div className="table-responsive">
          <table className="table table-hover">
            <thead><tr><th>Crop</th><th>Market</th><th>Location</th><th>Price</th><th>Unit</th><th>Date</th></tr></thead>
            <tbody>{prices.map(p => (
              <tr key={p.id}><td>{p.cropName}</td><td>{p.marketName}</td><td>{p.location}</td><td>&#8377;{p.price}</td><td>{p.unit}</td><td>{p.recordedDate}</td></tr>
            ))}</tbody>
          </table>
        </div>
      }
    </div>
  );
}
