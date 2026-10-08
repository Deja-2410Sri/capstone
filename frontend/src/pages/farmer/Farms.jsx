
import { useState, useEffect } from 'react';
import { farmAPI } from '../../services/api';

const emptyForm = {
  farmName: '',
  location: '',
  soilType: '',
  area: '',
  irrigationType: '',
  waterAvailability: '',
};

export default function FarmerFarms() {
  const [farms, setFarms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState(emptyForm);

  useEffect(() => {
    loadFarms();
  }, []);

  const loadFarms = async () => {
    try {
      const res = await farmAPI.getAll({ page: 0, size: 50 });
      setFarms(res.data.data.content || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const data = {
        ...formData,
        area: parseFloat(formData.area),
      };

      if (editingId) {
        await farmAPI.update(editingId, data);
        alert('Farm updated successfully');
      } else {
        await farmAPI.create(data);
        alert('Farm created successfully');
      }

      setShowForm(false);
      setEditingId(null);
      setFormData(emptyForm);
      loadFarms();
    } catch (err) {
      alert(err.response?.data?.message || 'Error saving farm');
    }
  };

  const handleEdit = (farm) => {
    setEditingId(farm.id);

    setFormData({
      farmName: farm.farmName || '',
      location: farm.location || '',
      soilType: farm.soilType || '',
      area: farm.area || '',
      irrigationType: farm.irrigationType || '',
      waterAvailability: farm.waterAvailability || '',
    });

    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this farm?')) return;

    try {
      await farmAPI.delete(id);
      alert('Farm deleted successfully');
      loadFarms();
    } catch (err) {
      alert(err.response?.data?.message || 'Error deleting farm');
    }
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingId(null);
    setFormData(emptyForm);
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border" />
      </div>
    );
  }

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>My Farms</h2>

        <button
          className="btn btn-primary"
          onClick={() => {
            setEditingId(null);
            setFormData(emptyForm);
            setShowForm(true);
          }}
        >
          + Add Farm
        </button>
      </div>

      {showForm && (
        <div className="card mb-4">
          <div className="card-body">
            <h5>{editingId ? 'Edit Farm' : 'Add New Farm'}</h5>

            <form onSubmit={handleSubmit} className="row g-3">
              {[
                'farmName',
                'location',
                'soilType',
                'area',
                'irrigationType',
                'waterAvailability',
              ].map((field) => (
                <div key={field} className="col-md-4">
                  <label className="form-label">
                    {field
                      .replace(/([A-Z])/g, ' $1')
                      .trim()}
                  </label>

                  <input
                    className="form-control"
                    required={
                      field === 'farmName' || field === 'area'
                    }
                    value={formData[field]}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        [field]: e.target.value,
                      })
                    }
                  />
                </div>
              ))}

              <div className="col-12">
                <button
                  type="submit"
                  className="btn btn-success me-2"
                >
                  {editingId ? 'Update' : 'Save'}
                </button>

                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={handleCancel}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {farms.length === 0 ? (
        <div className="alert alert-info">
          No farms added yet. Click "Add Farm" to get started.
        </div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover">
            <thead>
              <tr>
                <th>Name</th>
                <th>Location</th>
                <th>Soil Type</th>
                <th>Area (acres)</th>
                <th>Irrigation</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {farms.map((f) => (
                <tr key={f.id}>
                  <td>{f.farmName}</td>
                  <td>{f.location}</td>
                  <td>{f.soilType}</td>
                  <td>{f.area}</td>
                  <td>{f.irrigationType}</td>

                  <td>
                    <button
                      className="btn btn-warning btn-sm me-2"
                      onClick={() => handleEdit(f)}
                    >
                      Edit
                    </button>

                    <button
                      className="btn btn-danger btn-sm"
                      onClick={() => handleDelete(f.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
