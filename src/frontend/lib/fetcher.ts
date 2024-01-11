export async function fetcher<JSON = any>(url: RequestInfo | URL, options?: RequestInit) {
  const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL;

  if (!baseUrl) {
    throw new Error('API_BASE_URL is not defined');
  }

  const fullUrl = new URL(url.toString(), baseUrl);

  console.log('🚀 ~ file: fetcher.ts:13 ~ fullUrl:', fullUrl.toString());
  let reponse: Response;

  if (!options) {
    reponse = await fetch(fullUrl);
  } else {
    reponse = await fetch(fullUrl, options);
  }

  const data = await reponse.json();

  if (reponse.status !== 200) {
    throw new Error(data.message || 'Failed to fetch API');
  }

  return data;
}
