export default async function fetchJson<JSON = unknown>(
    input: RequestInfo,
    init?: RequestInit,
    apiURL?: string
): Promise<JSON> {
    const baseURL = apiURL || process.env.NEXT_PUBLIC_API_BASE_URL || "";
    init = init || {};

    let headers: any = {
        Accept: "application/json",
        "Accept-Language": "it",
    };

    init.headers = { ...init.headers, ...headers };

    const response = await fetch(baseURL + input, init);

    const data = await response.json();

    if (response.ok) {
        return data;
    }

    throw new FetchError({
        message: data.message,
        response,
        data,
    });
}

export class FetchError extends Error {
    response: Response;
    data: {
        message: string;
    };
    constructor({
                    message,
                    response,
                    data,
                }: {
        message: string;
        response: Response;
        data: {
            message: string;
        };
    }) {
        // Pass remaining arguments (including vendor specific ones) to parent constructor
        super(message);

        // Maintains proper stack trace for where our error was thrown (only available on V8)
        if (Error.captureStackTrace) {
            Error.captureStackTrace(this);
        }

        this.name = "FetchError";
        this.response = response;
        this.data = data ?? { message: message };
    }
}
