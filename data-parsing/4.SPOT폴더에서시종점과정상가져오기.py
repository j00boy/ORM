import json
import pandas as pd
from pyproj import Transformer
import os
import math 

def truncate_to_six_decimal_places(value):
    factor = 10 ** 6
    return math.floor(value * factor) / factor

def truncate_to_six_decimal_places_str(value):
    if '.' in value:
        integer_part, decimal_part = value.split('.')
        truncated_decimal_part = decimal_part[:6]  # 소수점 아래 6자리까지 자르기
        return float(f"{integer_part}.{truncated_decimal_part}")
    else:
        return float(value)  # 소수점이 없는 경우 원래 값 반환

### 분기점 정보 추출
import json
import pandas as pd
from pyproj import Transformer
import os

# 디렉토리 경로와 출력 파일 경로 설정
directory_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/commonPJT/DB/mountain/mountain/geojson/SPOT'
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/startAndPeak.csv'

# 좌표계 정의 (MNTN 좌표계와 WGS84 경위도 좌표계)
crs_MNTN = "epsg:5186"
crs_LATLON = "epsg:4326"

# Transformer 객체 생성
transformer = Transformer.from_crs(crs_MNTN, crs_LATLON, always_xy=True)

# 모든 JSON 파일을 처리하기 위한 리스트
all_points = []
global_index = 1

# 디렉토리 내 모든 JSON 파일을 읽어서 처리
for filename in os.listdir(directory_path):
    if filename.endswith('.json'):
        file_path = os.path.join(directory_path, filename)

        # JSON 파일 읽기
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)

        # 포인트 정보 파싱 및 변환하기
        for feature in data['features']:
            attributes = feature['attributes']
            manage_sp2 = attributes.get('MANAGE_SP2')
            if manage_sp2 in ['정상', '시종점']:
                mntn_code = attributes['MNTN_CODE']
                x = truncate_to_six_decimal_places_str(str(feature['geometry']['x']))
                y = truncate_to_six_decimal_places_str(str(feature['geometry']['y']))
                lon, lat = transformer.transform(x, y)
                all_points.append({
                    'Global_ID': global_index,
                    'MNTN_CODE': mntn_code,
                    'Latitude': truncate_to_six_decimal_places_str(str(lat)),
                    'Longitude': truncate_to_six_decimal_places_str(str(lon)),
                    'Entry_Point': manage_sp2
                })
                global_index += 1

# DataFrame 생성
df_all_points = pd.DataFrame(all_points)

# DataFrame 출력
print(df_all_points.head())

# 필요한 경우 CSV 파일로 저장
df_all_points.to_csv(output_file_path, index=False, encoding='utf-8-sig')

##################################################################