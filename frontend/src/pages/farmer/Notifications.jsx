import { useState, useEffect } from 'react';
import { notificationAPI } from '../../services/api';

export default function FarmerNotifications() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => { loadNotifications(); }, []);
  const loadNotifications = async () => { try { const res = await notificationAPI.getAll(); setNotifications(res.data.data || []); } catch (err) { console.error(err); } finally { setLoading(false); } };
  const markRead = async (id) => { await notificationAPI.markAsRead(id); loadNotifications(); };
  const markAllRead = async () => { await notificationAPI.markAllAsRead(); loadNotifications(); };
  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;
  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Notifications</h2>
        <button className="btn btn-outline-primary btn-sm" onClick={markAllRead}>Mark All Read</button>
      </div>
      {notifications.length === 0 ? <div className="alert alert-info">No notifications.</div> :
        <div className="list-group">{notifications.map(n => (
          <div key={n.id} className={`list-group-item ${!n.read ? 'list-group-item-light border-start border-primary border-3' : ''}`}>
            <div className="d-flex justify-content-between">
              <div><h6 className="mb-1">{n.title}</h6><p className="mb-1 small">{n.message}</p></div>
              <div className="text-end">
                <small className="text-muted">{new Date(n.createdAt).toLocaleDateString()}</small>
                {!n.read && <button className="btn btn-sm btn-outline-secondary ms-2" onClick={() => markRead(n.id)}>Read</button>}
              </div>
            </div>
          </div>
        ))}</div>
      }
    </div>
  );
}
