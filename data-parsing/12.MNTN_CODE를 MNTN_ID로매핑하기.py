import pandas as pd

# 파일 경로
result_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/trail.csv'
mntn_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/sss/mntn.csv'
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240805/trail.csv'

# CSV 파일 읽기
df_trail = pd.read_csv(result_file_path)
df_mntn = pd.read_csv(mntn_file_path)

# mountain_code를 key로, id를 value로 하는 딕셔너리 생성
coords_to_id = {
    row['mountain_code']: row['id'] for _, row in df_mntn.iterrows()
    }

# start_id와 end_id 컬럼 추가 및 매핑
df_trail['mountain_id'] = df_trail.apply(lambda row: coords_to_id.get(row['mountain_id']), axis=1)

# mountain_id가 매핑이 되지 않은 데이터는 삭제
df_trail = df_trail.dropna(subset=['mountain_id'])

# 결과 CSV 파일로 저장
df_trail.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Updated edges file saved to {output_file_path}")
