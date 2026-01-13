import CreateAnnouncementForm from "./CreateAnnouncementForm.jsx";

const AdminView = ({ onAnnouncementCreated }) => {
    return (
        <div className="admin-view">
            <h2>Panel Administratora</h2>
            <CreateAnnouncementForm onAnnouncementCreated={onAnnouncementCreated} />
        </div>
    );
};

export default AdminView;
