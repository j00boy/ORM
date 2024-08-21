import pandas as pd
import requests
import time

# CSV 파일 불러오기
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/final_peak_data.csv'
df = pd.read_csv(file_path)

# API 요청을 위한 URL 준비
url_prefix = 'https://api.open-elevation.com/api/v1/lookup?locations='

def fetch_elevation(lat, lon):
    chunk_url = f"{url_prefix}{lat},{lon}"
    retries = 3
    while retries > 0:
        try:
            response = requests.get(url=chunk_url, timeout=10)  # 타임아웃을 10초로 설정
            if response.status_code == 200:
                elevation = response.json()['results'][0]['elevation']
                return elevation
            else:
                print(f"Failed to retrieve data for ({lat}, {lon}): {response.status_code}")
                return None
        except requests.exceptions.RequestException as e:
            print(f"Request failed: {e}. Retrying...")
            retries -= 1
            time.sleep(5)  # 5초 대기 후 재시도
    return None

# 고도 데이터가 없는 좌표에 대해 API 호출
for i, row in df[df['Elevation'].isna()].iterrows():
    lat = row['Latitude']
    lon = row['Longitude']
    elevation = fetch_elevation(lat, lon)
    df.at[i, 'Elevation'] = elevation
    print(f"Processed {i+1} / {len(df)} coordinates")

# 업데이트된 데이터프레임을 새로운 CSV 파일로 저장
updated_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/final_peak_data.csv'
df.to_csv(updated_file_path, index=False, encoding='utf-8-sig')

print(f"업데이트된 데이터가 {updated_file_path}에 저장되었습니다.")