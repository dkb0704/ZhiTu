import urllib.request
import json
import os

token = os.getenv("FIGMA_TOKEN")
file_key = os.getenv("FIGMA_FILE_KEY", "A9bO0p5dN64EH07KjAs93w")

if not token:
    raise RuntimeError("Please set FIGMA_TOKEN before running this script.")

url = f"https://api.figma.com/v1/files/{file_key}"

req = urllib.request.Request(url)
req.add_header('X-Figma-Token', token)

try:
    with urllib.request.urlopen(req) as response:
        data = json.loads(response.read().decode('utf-8'))
        
        # Save to a file for inspection
        output_path = os.path.join(os.path.dirname(__file__), "figma_design.json")
        with open(output_path, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
            
        print(f"Successfully fetched Figma data and saved to {output_path}")
        
        # Print high-level page names
        if "document" in data and "children" in data["document"]:
            pages = data["document"]["children"]
            for page in pages:
                print(f"Page: {page['name']}")
                if "children" in page:
                    for frame in page["children"]:
                        print(f"  Frame: {frame['name']}")
                
except Exception as e:
    print(f"Error fetching Figma data: {e}")
