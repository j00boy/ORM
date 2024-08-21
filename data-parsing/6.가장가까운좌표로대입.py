import pandas as pd
from geopy.distance import great_circle

# 파일 경로 설정
unique_coordinates_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/allentrypoints.csv'
entrypoints_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/filtered_startAndPeak.csv'
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/refiltered_startAndPeak.csv'

# CSV 파일 읽기
unique_coordinates_df = pd.read_csv(unique_coordinates_path)
entrypoints_df = pd.read_csv(entrypoints_path)

# 가장 가까운 좌표를 찾는 함수
def find_closest_coordinates(row, unique_df):
    mntn_code = row['MNTN_CODE']
    entry_latitude = row['Latitude']
    entry_longitude = row['Longitude']
    
    # Latitude와 Longitude가 유효한지 확인
    if pd.isnull(entry_latitude) or pd.isnull(entry_longitude):
        return -1
    
    # MNTN_CODE로 분기점 좌표 필터링
    filtered_unique_df = unique_df[unique_df['MNTN_CODE'] == mntn_code]
    
    # 최소 거리와 가장 가까운 인덱스 초기화
    min_distance = float('inf')
    closest_index = -1
    
    # 필터링된 유니크 좌표 반복
    for idx, unique_row in filtered_unique_df.iterrows():
        unique_latitude = unique_row['Latitude']
        unique_longitude = unique_row['Longitude']
        
        # Latitude와 Longitude가 유효한지 확인
        if pd.isnull(unique_latitude) or pd.isnull(unique_longitude):
            continue
        
        # 거리 계산
        distance = great_circle((entry_latitude, entry_longitude), (unique_latitude, unique_longitude)).meters
        
        # 최소 거리와 가장 가까운 인덱스 업데이트
        if distance < min_distance:
            min_distance = distance
            closest_index = idx
    
    return closest_index

# 모든 엔트리 포인트를 가장 가까운 좌표에 추가
for _, entry_row in entrypoints_df.iterrows():
    entry_latitude = entry_row['Latitude']
    entry_longitude = entry_row['Longitude']
    
    # Latitude와 Longitude가 유효한지 확인
    if pd.isnull(entry_latitude) or pd.isnull(entry_longitude):
        continue
    
    closest_idx = find_closest_coordinates(entry_row, unique_coordinates_df)
    if closest_idx != -1:
        unique_coordinates_df.at[closest_idx, 'info'] = entry_row['info']

# 업데이트된 데이터프레임을 새로운 엑셀 파일로 저장
unique_coordinates_df.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Updated file saved to {output_file_path}")
