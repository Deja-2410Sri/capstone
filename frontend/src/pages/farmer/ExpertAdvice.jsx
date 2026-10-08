import { useState, useEffect } from 'react';
import { adviceAPI } from '../../services/api';

export default function FarmerAdvice() {
  const [advices, setAdvices] = useState([]);
  const [question, setQuestion] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => { loadAdvices(); }, []);

  const loadAdvices = async () => {
    try { const res = await adviceAPI.getAll({ page: 0, size: 50 }); setAdvices(res.data.data?.content || []); }
    catch (err) { console.error(err); } finally { setLoading(false); }
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); setSubmitting(true);
    try { await adviceAPI.ask({ question }); setQuestion(''); loadAdvices(); }
    catch (err) { alert(err.response?.data?.message || 'Error'); } finally { setSubmitting(false); }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;

  return (
    <div className="container py-4">
      <h2 className="mb-4">Expert Advice</h2>
      <div className="card mb-4"><div className="card-body">
        <h5>Ask an Expert</h5>
        <form onSubmit={handleSubmit}>
          <textarea className="form-control mb-2" rows={3} placeholder="Describe your question..." value={question} onChange={e => setQuestion(e.target.value)} required />
          <button type="submit" className="btn btn-primary" disabled={submitting}>{submitting ? 'Submitting...' : 'Submit Question'}</button>
        </form>
      </div></div>
      {advices.length === 0 ? <div className="alert alert-info">No questions yet.</div> :
        <div className="table-responsive">
          <table className="table table-hover">
            <thead><tr><th>Question</th><th>Status</th><th>Response</th><th>Date</th></tr></thead>
            <tbody>{advices.map(a => (
              <tr key={a.id}>
                <td className="small">{a.question}</td>
                <td><span className={`badge bg-${a.status === 'ANSWERED' ? 'success' : a.status === 'OPEN' ? 'warning' : 'secondary'}`}>{a.status}</span></td>
                <td className="small">{a.response || 'Awaiting response...'}</td>
                <td className="small">{new Date(a.askedAt).toLocaleDateString()}</td>
              </tr>
            ))}</tbody>
          </table>
        </div>
      }
    </div>
  );
}
