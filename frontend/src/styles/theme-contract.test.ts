import { readFileSync } from 'node:fs';
import { resolve } from 'node:path';

import { describe, expect, it } from 'vitest';

describe('dark theme contract', () => {
  const stylesheet = readFileSync(resolve(__dirname, 'index.css'), 'utf8');

  it('keeps tables, pagination, inputs and workflow surfaces on the ANN dark palette', () => {
    expect(stylesheet).toContain('--ann-text-primary: #f7f7ff');
    expect(stylesheet).toContain('.semi-table-cell-fixed-right');
    expect(stylesheet).toContain('.semi-page-item');
    expect(stylesheet).toContain('.semi-page-item-active');
    expect(stylesheet).toContain('.semi-input::placeholder');
    expect(stylesheet).toContain('.ann-workflow-theme');
    expect(stylesheet).toContain('rgba(139, 92, 246, .05)');
    expect(stylesheet).toContain('rgba(135, 145, 180, .14) .65px');
    expect(stylesheet).toContain('background-size: auto, 42px 42px');
  });
});
