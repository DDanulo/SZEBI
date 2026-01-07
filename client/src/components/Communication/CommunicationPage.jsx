import { useState } from 'react';
import { Link } from 'react-router-dom';
import ResidentView from "./ResidentView.jsx";
import AdminView from "./AdminView.jsx";

const CommunicationPage = () => {
    const [role, setRole] = useState('resident');
    const [residentViewKey, setResidentViewKey] = useState(0);

    const handleAnnouncementCreated = () => {
        setResidentViewKey(prevKey => prevKey + 1);
        setRole('resident');
    };

    return (
        <div className="p-4">
            <Link to="/" className="mb-4 text-blue-500 hover:underline block">
                &larr; Powrót do strony głównej
            </Link>
            <h1 className="text-4xl font-bold mb-6 text-center">Moduł Komunikacji</h1>
            <div className="flex justify-center gap-4 mb-6">
                <button
                    onClick={() => setRole('resident')}
                    className={`px-4 py-2 rounded ${role === 'resident' ? 'bg-blue-600 text-white' : 'bg-gray-300'}`}
                >
                    Widok Mieszkańca
                </button>
                <button
                    onClick={() => setRole('admin')}
                    className={`px-4 py-2 rounded ${role === 'admin' ? 'bg-blue-600 text-white' : 'bg-gray-300'}`}
                >
                    Widok Administratora
                </button>
            </div>

            <hr className="my-6" />

            {role === 'resident' && <ResidentView key={residentViewKey} />}
            {role === 'admin' && <AdminView onAnnouncementCreated={handleAnnouncementCreated} />}
        </div>
    );
};

export default CommunicationPage;
