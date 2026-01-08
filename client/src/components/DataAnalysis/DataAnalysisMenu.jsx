import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import ReportModule from './ReportModule';

const DataAnalysisMenu = () => {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen bg-gray-100 p-6">
            <div className="max-w-7xl mx-auto">
                {/* Przycisk powrotu do strony głównej */}
                <button
                    onClick={() => navigate('/')}
                    className="mb-6 flex items-center gap-2 text-gray-600 hover:text-gray-900 font-semibold transition bg-white px-4 py-2 rounded-lg shadow-sm hover:shadow-md"
                >
                    <ArrowLeft size={20} /> Wróć do strony głównej
                </button>

                {/* Nagłówek sekcji */}
                <div className="mb-6">
                    <h1 className="text-3xl font-bold text-gray-800">Analiza i Raportowanie</h1>
                    <p className="text-gray-500 mt-1">
                        Generuj wykresy historyczne oraz pobieraj raporty w formacie PDF.
                    </p>
                </div>

                {/* Bezpośrednie wyświetlenie modułu raportów */}
                <ReportModule />
            </div>
        </div>
    );
};

export default DataAnalysisMenu;