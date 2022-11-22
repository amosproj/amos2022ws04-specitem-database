import { fireEvent, render, screen } from '@testing-library/react';
import App from './App';
import Specitems from './pages/specitems_page';

test('test save item to Export', () => {
  const exportList = []
  render(<Specitems exportList={[]} setExportList={list => {exportList = list}}/>);
  const button = screen.getByTestId("saveExport");
  const list = screen.getByTestId("exportList");
  fireEvent.click(button);
  expect(exportList).toHaveLength(2);
})
