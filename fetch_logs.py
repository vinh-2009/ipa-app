import urllib.request
import json
import zipfile
import io

repo = 'dnnloveyou1804-beep/ipa-corelock'
url = f'https://api.github.com/repos/{repo}/actions/runs'
req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
with urllib.request.urlopen(req) as response:
    data = json.loads(response.read().decode())
    
runs = data.get('workflow_runs', [])
if not runs:
    print('No runs found')
    exit()

latest_run = runs[0]
print(f"Latest run: {latest_run['id']} status: {latest_run['status']} conclusion: {latest_run['conclusion']}")

log_url = latest_run['logs_url']
print(f'Fetching logs from {log_url}')
req = urllib.request.Request(log_url, headers={'User-Agent': 'Mozilla/5.0'})
try:
    with urllib.request.urlopen(req) as response:
        with zipfile.ZipFile(io.BytesIO(response.read())) as z:
            for filename in z.namelist():
                if 'build' in filename.lower():
                    print(f'\n--- {filename} ---')
                    content = z.read(filename).decode('utf-8', errors='ignore')
                    lines = content.split('\n')
                    for i, line in enumerate(lines):
                        if 'error:' in line.lower() or 'failed' in line.lower() or 'invalid' in line.lower():
                            print(f"{i}: {line}")
except Exception as e:
    print(f'Error fetching logs: {e}')
