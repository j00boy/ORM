### 좌표 정보 추출

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



# 디렉토리 경로
directory_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/commonPJT/DB/mountain/mountain/geojson'
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/allpoints.csv'

# 좌표계 정의 (MNTN 좌표계와 WGS84 경위도 좌표계)
crs_MNTN = "epsg:5186"  # Replace with the correct EPSG code for your source CRS
crs_LATLON = "epsg:4326"

# Transformer 객체 생성
transformer = Transformer.from_crs(crs_MNTN, crs_LATLON, always_xy=True)

# 모든 JSON 파일을 처리하기 위한 리스트
all_coordinates = []
global_index = 1
path_id = 1  # Initialize path_id globally

# 디렉토리 내 모든 JSON 파일을 읽어서 처리
for filename in os.listdir(directory_path):
    if filename.endswith('.json'):
        file_path = os.path.join(directory_path, filename)

        # JSON 파일 읽기
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)

        # 좌표값 파싱 및 변환하기
        for feature in data['features']:
            paths = feature['geometry']['paths']
            for path in paths:
                for coord in path:
                    lon, lat = transformer.transform(truncate_to_six_decimal_places(coord[0]), truncate_to_six_decimal_places(coord[1]))
                    all_coordinates.append({
                        'Global_Index': global_index,
                        'Path_ID': path_id,
                        'Latitude': truncate_to_six_decimal_places(lat),
                        'Longitude': truncate_to_six_decimal_places(lon)
                    })
                    global_index += 1
                path_id += 1  # Increase path_id only after processing a full path

# DataFrame 생성
df_all_coordinates = pd.DataFrame(all_coordinates)

# DataFrame 출력
print(df_all_coordinates.head())

# 필요한 경우 CSV 파일로 저장
df_all_coordinates.to_csv(output_file_path, index=False, encoding='utf-8-sig')

### 간선 정보 추출
import json
import pandas as pd
from pyproj import Transformer
import os

# 폴더 경로와 출력 파일 경로 설정
directory_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/commonPJT/DB/mountain/mountain/geojson'
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/alledges.csv'

# 좌표계 정의 (MNTN 좌표계와 WGS84 경위도 좌표계)
crs_MNTN = "epsg:5186"
crs_LATLON = "epsg:4326"

# Transformer 객체 생성
transformer = Transformer.from_crs(crs_MNTN, crs_LATLON, always_xy=True)

# 난이도 매핑 사전
difficulty_mapping = {
    "쉬움": 1,
    "중간": 2,
    "어려움": 3
}

# 모든 JSON 파일을 처리하기 위한 리스트
all_edges = []
path_id = 1  # Initialize path_id globally

# 디렉토리 내 모든 JSON 파일을 읽어서 처리
for filename in os.listdir(directory_path):
    if filename.endswith('.json'):
        file_path = os.path.join(directory_path, filename)

        # JSON 파일 읽기
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)

        # 간선 정보 파싱 및 변환하기
        for feature in data['features']:
            mntn_code = feature['attributes']['MNTN_CODE']
            pmntn_lt = feature['attributes']['PMNTN_LT']
            pmntn_dffl = feature['attributes']['PMNTN_DFFL']
            pmntn_dffl_num = difficulty_mapping.get(pmntn_dffl, 0)  # 매핑 사전을 사용하여 변환, 매핑되지 않으면 0
            pmntn_uppl = feature['attributes']['PMNTN_UPPL']
            paths = feature['geometry']['paths']
            for path in paths:
                if len(path) > 1:
                    start_coord = transformer.transform(truncate_to_six_decimal_places(path[0][0]), truncate_to_six_decimal_places(path[0][1]))
                    end_coord = transformer.transform(truncate_to_six_decimal_places(path[-1][0]), truncate_to_six_decimal_places(path[-1][1]))
                    all_edges.append({
                        'Path_ID': path_id,
                        'Start_Latitude': truncate_to_six_decimal_places(start_coord[1]),
                        'Start_Longitude': truncate_to_six_decimal_places(start_coord[0]),
                        'End_Latitude': truncate_to_six_decimal_places(end_coord[1]),
                        'End_Longitude': truncate_to_six_decimal_places(end_coord[0]),
                        'MNTN_CODE': mntn_code,
                        'PMNTN_LT': pmntn_lt,
                        'PMNTN_UPPL': pmntn_uppl,
                        'PMNTN_DFFL': pmntn_dffl_num
                    })
                path_id += 1

# DataFrame 생성
df_all_edges = pd.DataFrame(all_edges)

# DataFrame 출력
print(df_all_edges.head())

# 필요한 경우 CSV 파일로 저장
df_all_edges.to_csv(output_file_path, index=False, encoding='utf-8-sig')



# # Load the data from the uploaded CSV file
# file_path = 'C:/Users/SSAFY/Desktop/edge.xlsx'
# data = pd.read_excel(file_path)

# # Extract unique start and end points with MNTN_CODE
# start_points = data[['Start_Longitude', 'Start_Latitude', 'MNTN_CODE']].drop_duplicates()
# end_points = data[['End_Longitude', 'End_Latitude', 'MNTN_CODE']].drop_duplicates()

# # Rename columns to unify structure
# start_points = start_points.rename(columns={'Start_Longitude': 'Longitude', 'Start_Latitude': 'Latitude'})
# end_points = end_points.rename(columns={'End_Longitude': 'Longitude', 'End_Latitude': 'Latitude'})

# # Combine start and end points
# all_points_with_code = pd.concat([start_points, end_points]).drop_duplicates().reset_index(drop=True)

# # Assign unique IDs
# all_points_with_code['Global_ID'] = range(1, len(all_points_with_code) + 1)

# # Reorder columns to have Point_ID first
# all_points_with_code = all_points_with_code[['Global_ID', 'Longitude', 'Latitude', 'MNTN_CODE']]

# # Save the updated unique points with IDs and MNTN_CODE to a new CSV file
# unique_points_with_code_file_path = 'C:/Users/SSAFY/Desktop/commonPJT/DB/Data/entrypoints.csv'
# all_points_with_code.to_csv(unique_points_with_code_file_path, index=False,  encoding='utf-8-sig')

### 분기점 정보 추출
# import json
# import pandas as pd
# from pyproj import Transformer
# import os

# # 디렉토리 경로와 출력 파일 경로 설정
# directory_path = 'C:/Users/SSAFY/Desktop/commonPJT/DB/mountain/mountain/geojson/SPOT'
# output_file_path = 'C:/Users/SSAFY/Desktop/allentrypoints.csv'

# # 좌표계 정의 (MNTN 좌표계와 WGS84 경위도 좌표계)
# crs_MNTN = "epsg:5186"
# crs_LATLON = "epsg:4326"

# # Transformer 객체 생성
# transformer = Transformer.from_crs(crs_MNTN, crs_LATLON, always_xy=True)

# # 모든 JSON 파일을 처리하기 위한 리스트
# all_points = []
# global_index = 1

# # 디렉토리 내 모든 JSON 파일을 읽어서 처리
# for filename in os.listdir(directory_path):
#     if filename.endswith('.json'):
#         file_path = os.path.join(directory_path, filename)

#         # JSON 파일 읽기
#         with open(file_path, 'r', encoding='utf-8') as f:
#             data = json.load(f)

#         # 포인트 정보 파싱 및 변환하기
#         for feature in data['features']:
#             attributes = feature['attributes']
#             manage_sp2 = attributes.get('MANAGE_SP2')
#             if manage_sp2 in ['정상', '시종점']:
#                 mntn_id = attributes['MNTN_ID']
#                 x = truncate_to_six_decimal_places_str(str(feature['geometry']['x']))
#                 y = truncate_to_six_decimal_places_str(str(feature['geometry']['y']))
#                 lon, lat = transformer.transform(x, y)
#                 all_points.append({
#                     'Global_ID': global_index,
#                     'MNTN_ID': mntn_id,
#                     'Latitude': truncate_to_six_decimal_places_str(str(lat)),
#                     'Longitude': truncate_to_six_decimal_places_str(str(lon)),
#                     'Entry_Point': manage_sp2
#                 })
#                 global_index += 1

# # DataFrame 생성
# df_all_points = pd.DataFrame(all_points)

# # DataFrame 출력
# print(df_all_points.head())

# # 필요한 경우 CSV 파일로 저장
# df_all_points.to_csv(output_file_path, index=False, encoding='utf-8-sig')

##################################################################

# # 파일 경로
# input_file_path = 'C:/Users/SSAFY/Desktop/'
# output_file_path = '/mnt/data/drop_duplicates_entrypoint.csv'

# # 엑셀 파일 읽기
# df = pd.read_excel(input_file_path)

# # 필요한 컬럼 추출 및 중복 제거
# unique_points = set()

# for _, row in df.iterrows():
#     unique_points.add((row['Start_Latitude'], row['Start_Longitude']))
#     unique_points.add((row['End_Latitude'], row['End_Longitude']))

# # 중복 제거된 데이터프레임 생성
# df_unique_points = pd.DataFrame(list(unique_points), columns=['Latitude', 'Longitude'])

# # CSV 파일로 저장
# df_unique_points.to_csv(output_file_path, index=False, encoding='utf-8-sig')

# import ace_tools as tools; tools.display_dataframe_to_user(name="Unique Entry Points", dataframe=df_unique_points)

# df_unique_points.head()