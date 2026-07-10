import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import App from './App';
import data from './data.json';

// Mock the react-syntax-highlighter to avoid testing external lib rendering complexity
vi.mock('react-syntax-highlighter', () => ({
  Prism: ({ children }) => <pre data-testid="code-block">{children}</pre>
}));

describe('App Component', () => {
  it('renders the initial data structure correctly', () => {
    render(<App />);
    
    // Check if the logo exists
    expect(screen.getAllByText('DataStructures').length).toBeGreaterThan(0);

    if (data.length > 0) {
      // The first item should be active and rendered
      const firstDs = data[0];
      expect(screen.getAllByText(firstDs.name).length).toBeGreaterThan(0);
      expect(screen.getByText(firstDs.description)).toBeInTheDocument();
    } else {
      expect(screen.getByText('No Data Structures Found')).toBeInTheDocument();
    }
  });

  it('changes active data structure when sidebar button is clicked', () => {
    if (data.length < 2) return; // Skip test if not enough data
    
    render(<App />);
    
    const secondDs = data[1];
    
    // Find the button for the second data structure
    const btn = screen.getAllByRole('button', { name: new RegExp(secondDs.name, 'i') })[0];
    fireEvent.click(btn);

    // Verify content has updated
    // Title is usually an h1 or h2, checking for text in the main view
    expect(screen.getByText(secondDs.description)).toBeInTheDocument();
    expect(screen.getByText(secondDs.usage)).toBeInTheDocument();
  });

  it('toggles mobile sidebar correctly', () => {
    render(<App />);
    
    const toggleBtn = screen.getAllByRole('button')[0]; // The first button is the mobile menu toggle
    fireEvent.click(toggleBtn);
    
    // Check if sidebar gets "open" class (though jsdom doesn't compute CSS visual layout,
    // we can check if the overlay is rendered)
    const overlay = document.querySelector('.sidebar-overlay');
    expect(overlay).toBeInTheDocument();

    // Click it again to close
    fireEvent.click(overlay);
    expect(document.querySelector('.sidebar-overlay')).not.toBeInTheDocument();
  });
});

describe('App Component with Empty Data', () => {
  it('renders empty state correctly when no data structures exist', () => {
    render(<App initialData={[]} />);
    expect(screen.getByText('No Data Structures Found')).toBeInTheDocument();
  });
});
