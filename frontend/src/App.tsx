import { useState } from 'react';
import MensajeForm from './components/MensajeForm';
import MensajeList from './components/MensajeList';
import './App.css';

function App() {
  const [refreshKey, setRefreshKey] = useState(false);

  const handleRefresh = () => {
    setRefreshKey(prev => !prev);
  };

  const handleRefreshComplete = () => {};

  return (
    <div className="app">
      <header className="app-header">
        <h1>SmartBook - Comunicacion</h1>
      </header>

      <main className="app-content">
        <MensajeForm onMensajeCreado={handleRefresh} />
        <MensajeList refresh={refreshKey} onRefreshComplete={handleRefreshComplete} />
      </main>
    </div>
  );
}

export default App;
