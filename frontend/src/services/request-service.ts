export type ApiErrorKind = 'business' | 'http' | 'network' | 'parse';

export interface ApiEnvelope<T> {
  code: string;
  info: string;
  data: T;
}

export class ApiRequestError extends Error {
  readonly kind: ApiErrorKind;
  readonly status?: number;
  readonly code?: string;

  constructor(
    message: string,
    options: { kind: ApiErrorKind; status?: number; code?: string }
  ) {
    super(message);
    this.name = 'ApiRequestError';
    this.kind = options.kind;
    this.status = options.status;
    this.code = options.code;
    Object.setPrototypeOf(this, ApiRequestError.prototype);
  }
}

type Fetcher = (input: RequestInfo | URL, init?: RequestInit) => Promise<Response>;

export const requestJson = async <T>(
  input: RequestInfo | URL,
  init: RequestInit = {},
  fetcher: Fetcher = fetch
): Promise<T> => {
  let response: Response;

  try {
    response = await fetcher(input, init);
  } catch {
    throw new ApiRequestError('网络连接失败，请稍后重试', { kind: 'network' });
  }

  if (!response.ok) {
    throw new ApiRequestError(`请求失败（HTTP ${response.status}）`, {
      kind: 'http',
      status: response.status,
    });
  }

  let envelope: ApiEnvelope<T>;

  try {
    envelope = (await response.json()) as ApiEnvelope<T>;
  } catch {
    throw new ApiRequestError('服务返回了无法解析的数据', { kind: 'parse' });
  }

  if (envelope.code !== '0000') {
    throw new ApiRequestError(envelope.info || '操作失败', {
      kind: 'business',
      code: envelope.code,
    });
  }

  return envelope.data;
};
