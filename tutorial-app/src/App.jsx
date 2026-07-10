import React, { useState } from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { Book, Code, Info, Play, Menu, X, Terminal, Database } from 'lucide-react';
import data from './data.json';
import './App.css';

function App() {
  const [activeDs, setActiveDs] = useState(data.length > 0 ? data[0] : null);
  const [isSidebarOpen, setSidebarOpen] = useState(false);

  const toggleSidebar = () => setSidebarOpen(!isSidebarOpen);

  return (
    <div className="app-container">
      {/* Mobile Header */}
      <header className="mobile-header">
        <div className="logo-container">
          <Database className="logo-icon" />
          <h1>DataStructures</h1>
        </div>
        <button className="menu-btn" onClick={toggleSidebar}>
          {isSidebarOpen ? <X size={24} /> : <Menu size={24} />}
        </button>
      </header>

      {/* Sidebar */}
      <nav className={`sidebar ${isSidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-header desktop-only">
          <Database className="logo-icon" />
          <h2>DataStructures</h2>
        </div>
        <ul className="nav-list">
          {data.map((ds) => (
            <li key={ds.id}>
              <button 
                className={`nav-btn ${activeDs?.id === ds.id ? 'active' : ''}`}
                onClick={() => {
                  setActiveDs(ds);
                  setSidebarOpen(false);
                }}
              >
                <Terminal size={18} />
                <span>{ds.name}</span>
              </button>
            </li>
          ))}
        </ul>
        <div className="sidebar-footer">
          <p>{data.length} structures loaded</p>
        </div>
      </nav>

      {/* Main Content */}
      <main className="main-content">
        {activeDs ? (
          <div className="content-wrapper fade-in" key={activeDs.id}>
            <header className="content-header">
              <h1 className="ds-title">{activeDs.name}</h1>
            </header>

            <section className="info-card glass-panel">
              <div className="card-header">
                <Info className="card-icon" />
                <h3>What it does</h3>
              </div>
              <p className="card-text">{activeDs.description}</p>
            </section>

            <section className="info-card glass-panel">
              <div className="card-header">
                <Play className="card-icon" />
                <h3>Where it should be used</h3>
              </div>
              <p className="card-text">{activeDs.usage}</p>
            </section>

            <section className="info-card glass-panel">
              <div className="card-header">
                <Book className="card-icon" />
                <h3>Summary of implementation</h3>
              </div>
              <p className="card-text">{activeDs.summary}</p>
            </section>

            <section className="code-section glass-panel">
              <div className="card-header">
                <Code className="card-icon" />
                <h3>Source Code</h3>
              </div>
              <div className="code-container">
                <SyntaxHighlighter 
                  language="java" 
                  style={vscDarkPlus} 
                  showLineNumbers
                  customStyle={{
                    margin: 0,
                    padding: '1.5rem',
                    borderRadius: '0 0 12px 12px',
                    background: '#1e1e2e',
                    fontSize: '0.9rem',
                    lineHeight: '1.5'
                  }}
                >
                  {activeDs.code}
                </SyntaxHighlighter>
              </div>
            </section>
          </div>
        ) : (
          <div className="empty-state">
            <Database size={64} className="empty-icon" />
            <h2>No Data Structures Found</h2>
            <p>Add some Java files with proper Javadoc tags to see them here.</p>
          </div>
        )}
      </main>

      {/* Overlay for mobile sidebar */}
      {isSidebarOpen && <div className="sidebar-overlay" onClick={toggleSidebar}></div>}
    </div>
  );
}

export default App;
