import pandas as pd

# 파일 경로
input_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/alledges.csv'
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/allentrypoints.csv'

# CSV 파일 읽기
df = pd.read_csv(input_file_path)

# 필요한 컬럼 추출 및 중복 제거
vertex_map = {}
id_counter = 1

for _, row in df.iterrows():
    mountain_id = row['MNTN_CODE']
    start_coord = (row['Start_Latitude'], row['Start_Longitude'])
    end_coord = (row['End_Latitude'], row['End_Longitude'])

    if start_coord not in vertex_map:
        vertex_map[start_coord] = [id_counter, mountain_id]
        id_counter += 1

    if end_coord not in vertex_map:
        vertex_map[end_coord] = [id_counter, mountain_id]
        id_counter += 1

# 데이터프레임 생성 및 id와 info 컬럼 추가
vertices = [[vid, mountain_id, lat, lon, ""] for (lat, lon), (vid, mountain_id) in vertex_map.items()]
df_vertices = pd.DataFrame(vertices, columns=['id', 'mountain_code', 'latitude', 'longitude', 'info'])

# CSV 파일로 저장
df_vertices.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Vertices extraction completed. Total vertices: {len(df_vertices)}")
