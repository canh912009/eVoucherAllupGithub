export function parseHtmlToText(rawHtml: string): string {
  // Create a temporary div element
  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = rawHtml;

  // Extract text content from the div
  const textContent = tempDiv.textContent ?? '';

  // Clean up and return
  tempDiv.remove();
  return textContent.trim();
}
