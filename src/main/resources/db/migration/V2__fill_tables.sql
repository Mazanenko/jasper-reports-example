INSERT INTO reports.document (name, type, content, status, customer, supplier, created, modified)
VALUES
    ('document 100','T1','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_1','FACTORY_3','2021-01-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 101','T2','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_2','FACTORY_3','2021-01-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 102','T3','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_3','FACTORY_4','2021-01-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 103','T1','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_4','FACTORY_4','2022-02-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 104','T2','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_5','FACTORY_5','2022-02-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 105','T3','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_5','FACTORY_5','2022-03-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 106','T1','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_2','FACTORY_5','2022-03-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 107','T2','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_2','FACTORY_4','2022-03-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 108','T2','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_5','FACTORY_1','2022-01-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 109','T3','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_3','FACTORY_1','2022-01-26T01:18+03:00','2022-04-10T04:53+03:00'),
    ('document 110','T3','congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum','NO_VIEWED','ORGANIZATION_3','FACTORY_6','2022-01-26T01:18+03:00','2022-04-10T04:53+03:00');

INSERT INTO reports.template (name, link, type)
VALUES
('template1', 'src/main/resources/jrxmlTemplate/template1.jrxml', 'T1'),
('template2', 'src/main/resources/jrxmlTemplate/template2.jrxml', 'T2'),
('template3', 'src/main/resources/jrxmlTemplate/template3.jrxml', 'T3');