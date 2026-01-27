-- Seed inicial de produtos para demonstração
INSERT INTO products (id, name, description, price, stock_quantity, active, version, created_at, updated_at)
VALUES
    ('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d', 'Notebook Dell XPS 15', 'Notebook premium com tela OLED 15.6"', 8999.90, 12, true, 0, NOW(), NOW()),
    ('b2c3d4e5-f6a7-5b6c-9d0e-1f2a3b4c5d6e', 'Mouse Logitech MX Master 3', 'Mouse ergonômico sem fio', 599.90, 45, true, 0, NOW(), NOW()),
    ('c3d4e5f6-a7b8-6c7d-0e1f-2a3b4c5d6e7f', 'Teclado Mecânico Keychron K2', 'Teclado mecânico 75% wireless', 449.90, 30, true, 0, NOW(), NOW()),
    ('d4e5f6a7-b8c9-7d8e-1f2a-3b4c5d6e7f8a', 'Monitor LG UltraWide 34"', 'Monitor curvo 34" 144Hz', 3499.90, 8, true, 0, NOW(), NOW()),
    ('e5f6a7b8-c9d0-8e9f-2a3b-4c5d6e7f8a9b', 'Headset HyperX Cloud II', 'Headset gamer 7.1 surround', 499.90, 25, true, 0, NOW(), NOW()),
    ('f6a7b8c9-d0e1-9f0a-3b4c-5d6e7f8a9b0c', 'Webcam Logitech C920', 'Webcam Full HD 1080p', 399.90, 18, true, 0, NOW(), NOW()),
    ('a7b8c9d0-e1f2-0a1b-4c5d-6e7f8a9b0c1d', 'SSD Samsung 970 EVO 1TB', 'SSD NVMe M.2 alta performance', 699.90, 35, true, 0, NOW(), NOW()),
    ('b8c9d0e1-f2a3-1b2c-5d6e-7f8a9b0c1d2e', 'Cadeira Gamer ThunderX3', 'Cadeira ergonômica reclinável', 1299.90, 6, true, 0, NOW(), NOW()),
    ('c9d0e1f2-a3b4-2c3d-6e7f-8a9b0c1d2e3f', 'Mesa Gamer RGB', 'Mesa com iluminação RGB integrada', 899.90, 4, true, 0, NOW(), NOW()),
    ('d0e1f2a3-b4c5-3d4e-7f8a-9b0c1d2e3f4a', 'Ring Light 26cm', 'Ring light profissional com tripé', 149.90, 50, true, 0, NOW(), NOW()),
    ('e1f2a3b4-c5d6-4e5f-8a9b-0c1d2e3f4a5b', 'Hub USB-C 7 em 1', 'Hub multiportas HDMI/USB/SD', 189.90, 40, true, 0, NOW(), NOW()),
    ('f2a3b4c5-d6e7-5f6a-9b0c-1d2e3f4a5b6c', 'Mousepad XXL Gamer', 'Mousepad grande 90x40cm', 79.90, 60, true, 0, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
