import { useState, useEffect } from 'react';
import { adminAPI } from '../../services/api';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => { adminAPI.getUsers({ page: 0, size: 50 }).then(res => setUsers(res.data.data?.content || [])).catch(console.error).finally(() => setLoading(false)); }, []);
  if (loading) return <div className="text-center mt-5"><div className="spinner-border" /></div>;
  return (
    <div className="container py-4">
      <h2 className="mb-4">User Management</h2>
      <div className="table-responsive">
        <table className="table table-hover">
          <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Status</th></tr></thead>
          <tbody>{users.map(u => (
            <tr key={u.id}><td>{u.id}</td><td>{u.firstName} {u.lastName}</td><td>{u.email}</td>
              <td><span className={`badge bg-${u.role === 'ROLE_ADMIN' ? 'danger' : u.role === 'ROLE_EXPERT' ? 'info' : 'success'}`}>{u.role?.replace('ROLE_', '')}</span></td>
              <td><span className={`badge bg-${u.enabled ? 'success' : 'secondary'}`}>{u.enabled ? 'Active' : 'Disabled'}</span></td></tr>
          ))}</tbody>
        </table>
      </div>
    </div>
  );
}
