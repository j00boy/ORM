import pandas as pd
import requests
import time

# CSV 파일 불러오기
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/entry_point_peak.csv'  # 로컬 파일 경로
df = pd.read_csv(file_path)

# API 요청을 위한 URL 준비
url_prefix = 'https://api.open-elevation.com/api/v1/lookup?locations='

# API 제한을 피하기 위해 청크 단위로 고도 데이터 가져오기
chunk_size = 3
elevation_data = []

def fetch_elevation_data(chunk):
    coordinate_strings = '|'.join([f"{lat},{lon}" for lat, lon in chunk])
    chunk_url = url_prefix + coordinate_strings
    retries = 3
    while retries > 0:
        try:
            response = requests.get(url=chunk_url, timeout=10)  # 타임아웃을 10초로 설정
            if response.status_code == 200:
                elevations = response.json()['results']
                return [result['elevation'] for result in elevations]
            else:
                print(f"Failed to retrieve data for chunk {chunk}: {response.status_code}")
                return [None] * len(chunk)
        except requests.exceptions.RequestException as e:
            print(f"Request failed: {e}. Retrying...")
            retries -= 1
            time.sleep(5)  # 5초 대기 후 재시도
    return [None] * len(chunk)

# 위도와 경도 좌표를 리스트로 추출
coordinates = df[['Latitude', 'Longitude']].dropna().values.tolist()

for i in range(0, len(coordinates), chunk_size):
    chunk = coordinates[i:i + chunk_size]
    elevation_data.extend(fetch_elevation_data(chunk))
    print(f"Processed {i + len(chunk)} / {len(coordinates)} coordinates")

# 데이터프레임에 고도 데이터 추가
elevation_index = df.dropna(subset=['Latitude', 'Longitude']).index
if len(elevation_data) == len(elevation_index):
    df.loc[elevation_index, 'Elevation'] = elevation_data
else:
    print(f"Length mismatch: expected {len(elevation_index)}, got {len(elevation_data)}")

# 업데이트된 데이터프레임을 새로운 CSV 파일로 저장
updated_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/multiple_peak_with_elevation.csv'  # 로컬 파일 경로
df.to_csv(updated_file_path, index=False, encoding='utf-8-sig')

print(f"업데이트된 데이터가 {updated_file_path}에 저장되었습니다.")
