export const theme = {
  colors: {
    primary: '#8b5cf6',
    primaryHover: '#a78bfa',
    primaryActive: '#7c3aed',
    cyan: '#22d3ee',
    success: '#34d399',
    warning: '#fbbf24',
    error: '#fb7185',
    info: '#38bdf8',
    text: {
      primary: '#f7f7ff',
      secondary: '#a7acc0',
      tertiary: '#747b94',
      disabled: '#50566c',
    },
    bg: {
      primary: '#070910',
      secondary: '#0c0f19',
      tertiary: '#121624',
      elevated: '#171b2b',
      disabled: '#151825',
    },
    border: {
      primary: 'rgba(151, 160, 198, 0.18)',
      secondary: 'rgba(151, 160, 198, 0.11)',
      tertiary: 'rgba(139, 92, 246, 0.32)',
    },
    gradient: {
      primary: 'linear-gradient(135deg, #8b5cf6 0%, #6d5dfc 100%)',
      secondary: 'linear-gradient(135deg, #22d3ee 0%, #8b5cf6 100%)',
      tertiary: 'linear-gradient(135deg, #34d399 0%, #22d3ee 100%)',
    },
  },
  typography: {
    fontFamily: 'Inter, "SF Pro Display", "Segoe UI", "PingFang SC", sans-serif',
    fontSize: { xs: '12px', sm: '13px', base: '14px', lg: '16px', xl: '20px', '2xl': '24px', '3xl': '30px', '4xl': '38px' },
    fontWeight: { normal: 400, medium: 500, semibold: 600, bold: 700 },
    lineHeight: { tight: 1.25, normal: 1.55, relaxed: 1.75 },
  },
  spacing: { xs: '4px', sm: '8px', base: '16px', lg: '24px', xl: '32px', '2xl': '48px', '3xl': '64px' },
  borderRadius: { sm: '6px', base: '10px', lg: '14px', xl: '18px', '2xl': '24px', full: '999px' },
  shadows: {
    sm: '0 4px 12px rgba(0, 0, 0, 0.16)',
    base: '0 10px 30px rgba(0, 0, 0, 0.22)',
    md: '0 16px 44px rgba(0, 0, 0, 0.28)',
    lg: '0 22px 64px rgba(0, 0, 0, 0.34)',
    xl: '0 30px 90px rgba(0, 0, 0, 0.4)',
    card: '0 14px 36px rgba(0, 0, 0, 0.2)',
    modal: '0 28px 90px rgba(0, 0, 0, 0.5)',
  },
  animation: {
    duration: { fast: '120ms', normal: '200ms', slow: '320ms' },
    easing: { ease: 'ease', easeIn: 'ease-in', easeOut: 'ease-out', easeInOut: 'ease-in-out', cubic: 'cubic-bezier(0.22, 1, 0.36, 1)' },
  },
  breakpoints: { sm: '640px', md: '768px', lg: '1024px', xl: '1280px', '2xl': '1536px' },
  zIndex: { dropdown: 1000, sticky: 1020, fixed: 1030, modal: 1040, popover: 1050, tooltip: 1060 },
} as const;

export type Theme = typeof theme;
