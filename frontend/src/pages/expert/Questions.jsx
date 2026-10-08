import { useState, useEffect } from 'react';
import { adviceAPI } from '../../services/api';

export default function ExpertQuestions() {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [answers, setAnswers] = useState({});

  useEffect(() => { loadQuestions(); }, []);

  const loadQuestions = async () => {
    try { const res = await adviceAPI.getAll({ page: 0, size: 50 }); setQuestions(res.data.data?.content || []); }
    catch (err) { console.error(err); } finally { setLoading(false); }
  };

  const handleAnswer = async (id) => {
    const response = answers[id];
    if (!response) return;
    try { await adviceAPI.answer(id, { response }); setAnswers({...answers, [id]: ''}); loadQuestions(); }
    catch (err) { alert(err.response?.data?.message || 'Error answering question'); }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;
  return (
    <div className="container py-4">
      <h2 className="mb-4">Farmer Questions</h2>
      {questions.length === 0 ? <div className="alert alert-info">No questions pending.</div> :
        questions.map(q => (
          <div key={q.id} className="card mb-3">
            <div className="card-body">
              <div className="d-flex justify-content-between"><h6>From: {q.farmerName}</h6>
                <span className={`badge bg-${q.status === 'ANSWERED' ? 'success' : 'warning'}`}>{q.status}</span></div>
              <p>{q.question}</p>
              {q.response && <div className="alert alert-success mb-2"><strong>Your Response:</strong> {q.response}</div>}
              {q.status === 'OPEN' && (
                <div className="input-group">
                  <input className="form-control" placeholder="Type your answer..." value={answers[q.id] || ''}
                    onChange={e => setAnswers({...answers, [q.id]: e.target.value})} />
                  <button className="btn btn-success" onClick={() => handleAnswer(q.id)}>Submit Answer</button>
                </div>
              )}
            </div>
          </div>
        ))
      }
    </div>
  );
}
