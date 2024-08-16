import pandas as pd

# 파일 경로
edges_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/alledges.csv'
entrypoints_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/allentrypoints.csv'
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/alledges_with_id.csv'

# CSV 파일 읽기
df_edges = pd.read_csv(edges_file_path)
df_entrypoints = pd.read_csv(entrypoints_file_path)

# 좌표를 key로, id를 value로 하는 딕셔너리 생성
coords_to_id = {(row['latitude'], row['longitude']): row['id'] for _, row in df_entrypoints.iterrows()}

# start_id와 end_id 컬럼 추가 및 매핑
df_edges['start_id'] = df_edges.apply(lambda row: coords_to_id.get((row['Start_Latitude'], row['Start_Longitude'])), axis=1)
df_edges['end_id'] = df_edges.apply(lambda row: coords_to_id.get((row['End_Latitude'], row['End_Longitude'])), axis=1)

# 결과 CSV 파일로 저장
df_edges.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Updated edges file saved to {output_file_path}")
