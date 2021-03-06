CREATE OR REPLACE PROCEDURE FMOB000101
/********************************************************************/
/*  �֐����FFMOB000101.sql                                          */
/*  �@�\���FAOBA�]���Q��(AOBA�]���X��وꎞð��ّ}��)                */
/*  �쐬�ҁFTIS��{                                                 */
/*  �쐬���F2007/12/13                                              */
/*  �C���ҁFTIS��{                                                 */
/*  �C�����F2008/01/30                                              */
/*           15,25 �e�X�o�^�Ή�                                     */
/*  �C���ҁFTIS�匴                                                 */
/*  �C�����F2010/08/19                                              */
/*           �������X�Ή�                                           */
/*------------------------------------------------------------------*/
/*  �����F                                                          */
/*  �ߒl�F                                                          */
/********************************************************************/
IS
BEGIN

EXECUTE IMMEDIATE 'TRUNCATE TABLE WK_AOBA_TNS_YBILL';


-- AOBA�]���p�Ώۃf�[�^�쐬


INSERT INTO WK_AOBA_TNS_YBILL
        (--������ �ڋq����
        FIT_KKYK_CD,
        --�XBILL�t���O_15�Q
        YBILL_FLG_15G,
        --�XBILL�t���O_25�Q
        YBILL_FLG_25G,
        --�����ԍ��\���敪_15�Q
        AITE_BNG_HYJI_KBN_15G,
        --�����ԍ��\���敪_25�Q
        AITE_BNG_HYJI_KBN_25G,
        --ebill�t���O_15�Q
        EBILL_FLG_15G,
        --ebill�t���O_25�Q
        EBILL_FLG_25G)
SELECT  DISTINCT
        A.KKYK_CD,
        ' ',
        ' ',
        ' ',
        ' ',
        ' ',
        ' '
FROM    --������
        TB_SEIQ A
WHERE   --������D�폜�׸� = 0 (���폜)
        A.SAKJO_FLG = '0'
        --������D�K�p�ð�� = 1(�^�p��)
AND     A.TEKIYO_STAT = '1'
        ;



-- �XBILL�t���O���(15�Q)���Z�b�g

UPDATE  WK_AOBA_TNS_YBILL A
    SET (
        --�XBILL�t���O_15�Q
        YBILL_FLG_15G,
        --�����ԍ��\���敪_15�Q
        AITE_BNG_HYJI_KBN_15G
        )=(
        SELECT  --�������߼��Ͻ�.�XBILL�t���O_15�Q
                C.YBILL_FLG_15G,
                --�������߼��Ͻ�.�����ԍ��\���敪_15�Q
                C.AITE_BNG_HYJI_KBN_15G
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --�������߼��Ͻ�.�XBILL�t���O_15�Q = ' '
        AND     C.YBILL_FLG_15G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --�������߼��Ͻ�.�XBILL�t���O_15�Q = ' '
        AND     C.YBILL_FLG_15G != ' '
        );


-- �XBILL�t���O���(25�Q)���Z�b�g
UPDATE  WK_AOBA_TNS_YBILL A
    SET (
        --�XBILL�t���O_25�Q
        YBILL_FLG_25G,
        --�����ԍ��\���敪_25�Q
        AITE_BNG_HYJI_KBN_25G
        )=(
        SELECT
                --�������߼��Ͻ�.�XBILL�t���O_25�Q
                C.YBILL_FLG_25G,
                --�������߼��Ͻ�.�����ԍ��\���敪_25�Q
                C.AITE_BNG_HYJI_KBN_25G
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --�������߼��Ͻ�.�XBILL�t���O_25�Q = ' '
        AND     C.YBILL_FLG_25G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --�������߼��Ͻ�.�XBILL�t���O_25�Q = ' '
        AND     C.YBILL_FLG_25G != ' '
        );


--�����ԍ��\���敪���(15�Q)���Z�b�g
UPDATE  WK_AOBA_TNS_YBILL A
    SET --�����ԍ��\���敪_15�Q
        AITE_BNG_HYJI_KBN_15G
        =(
        SELECT  --�������߼��Ͻ�.�����ԍ��\���敪_15�Q
                C.AITE_BNG_HYJI_KBN_15G
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '2'
                --�������߼��Ͻ�.�����ԍ��\���敪_15�Q = ' '
        AND     C.AITE_BNG_HYJI_KBN_15G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 2
        AND     C.HANEI_JNJ_FLG = '2'
                --�������߼��Ͻ�.�����ԍ��\���敪_15�Q = ' '
        AND     C.AITE_BNG_HYJI_KBN_15G != ' '
        );

--�����ԍ��\���敪���(25�Q)���Z�b�g
UPDATE  WK_AOBA_TNS_YBILL A
    SET --�����ԍ��\���敪_25�Q
        AITE_BNG_HYJI_KBN_25G
        =(
        SELECT  --�������߼��Ͻ�.�����ԍ��\���敪_25�Q
                C.AITE_BNG_HYJI_KBN_25G
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '2'
                --�������߼��Ͻ�.�����ԍ��\���敪_25�Q = ' '
        AND     C.AITE_BNG_HYJI_KBN_25G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 2
        AND     C.HANEI_JNJ_FLG = '2'
                --�������߼��Ͻ�.�����ԍ��\���敪_25�Q = ' '
        AND     C.AITE_BNG_HYJI_KBN_25G != ' '
        );




--e-bill�t���O���(15�Q)���Z�b�g
UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --ebill�t���O_15�Q
        EBILL_FLG_15G
        =(
        SELECT  --�������߼��Ͻ�.�����ԍ��\���敪_15�Q
                C.EBILL_FLG_15G
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '3'
                --�������߼��Ͻ�.ebill�t���O_15�Q = ' '
        AND     C.EBILL_FLG_15G != ' '

        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 3
        AND     C.HANEI_JNJ_FLG = '3'
                --�������߼��Ͻ�.ebill�t���O_15�Q = ' '
        AND     C.EBILL_FLG_15G != ' '
        );


--e-bill�t���O���(25�Q)���Z�b�g
UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --ebill�t���O_25�Q
        EBILL_FLG_25G
        =(
        SELECT  --�������߼��Ͻ�.�����ԍ��\���敪_25�Q
                C.EBILL_FLG_25G
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 1
        AND     C.HANEI_JNJ_FLG = '3'
                --�������߼��Ͻ�.ebill�t���O_25�Q = ' '
        AND     C.EBILL_FLG_25G != ' '

        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������E��߼�� �ڰ���,�������߼��Ͻ�
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�ڋq����
                --  = ������E��߼�� �ڰ��݁D�ڋq����
                A.FIT_KKYK_CD = B.KKYK_CD
                --������E��߼�� �ڰ��݁D�������߼�ݺ���
                --  = �������߼��Ͻ��D�������߼�ݺ���
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --������E��߼�� �ڰ��݁D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������E��߼�� �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --�������߼��Ͻ��D���f�����׸� = 3
        AND     C.HANEI_JNJ_FLG = '3'
                --�������߼��Ͻ�.ebill�t���O_25�Q = ' '
        AND     C.EBILL_FLG_25G != ' '
        );


-- �XBILL�t���O���(15�Q)���Z�b�g(�������X�ΏۊOOEM)

UPDATE  WK_AOBA_TNS_YBILL A
    SET --�XBILL�t���O_15�Q
        A.YBILL_FLG_15G = '8'
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������,OEMϽ�
                TB_SEIQ B,TB_OEM_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�XBILL�t���O_15�Q = ' '
                A.YBILL_FLG_15G = ' '
                --AOBA�]���X��وꎞTBL�D�ڋq���� = ������D�ڋq����
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --������D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --������DOEM���� = OEMϽ��DOEM����
        AND     B.OEM_CD = C.OEM_CD
                --OEMϽ��D����ڽ�׸� = 0(����ڽ�ΏۊO)
        AND     C.SEIQ_LESS_FLG = '0'
                --OEMϽ��D�L���׸� = 1(�L��)
        AND     C.YUKO_FLG = '1'
        );

-- �XBILL�t���O���(25�Q)���Z�b�g(�������X�ΏۊOOEM)
UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --�XBILL�t���O_25�Q
        A.YBILL_FLG_25G = '8'
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������,OEMϽ�
                TB_SEIQ B,TB_OEM_MST C
        WHERE   --AOBA�]���X��وꎞTBL�D�XBILL�t���O_25�Q = ' '
                A.YBILL_FLG_25G = ' '
                --AOBA�]���X��وꎞTBL�D�ڋq���� = ������D�ڋq����
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --������D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --������D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --������DOEM���� = OEMϽ��DOEM����
        AND     B.OEM_CD = C.OEM_CD
                --OEMϽ��D���ށD����ڽ�׸� = 0(����ڽ�ΏۊO)
        AND     C.SEIQ_LESS_FLG = '0'
                --OEMϽ��D�L���׸� = 1(�L��)
        AND     C.YUKO_FLG = '1'
        );


-- �XBILL�t���O���(15�Q)���Z�b�g(�x����ʂ�����ڽ�ΏۊO)

UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --�XBILL�t���O_15�Q
        A.YBILL_FLG_15G = ' '
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������,OEM��x����ʥ������ �ڰ���,�x�����
                TB_SEIQ B, TB_OEM_HARAI_SEIQ_R C, TB_HARAI D
        WHERE   --AOBA�]���X��وꎞTBL�D�XBILL�t���O_15�Q = 7
                A.YBILL_FLG_15G = '7'
                --AOBA�]���X��وꎞTBL�D�ڋq���� = ������D�ڋq����
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --OEM��x����ʥ������ �ڰ��݁D�ڋq���� = ������D�ڋq����
        AND     C.KKYK_CD = B.KKYK_CD
                --OEM��x����ʥ������ �ڰ��݁DOEM���� = ������DOEM����
        AND     C.OEM_CD = B.OEM_CD
                --������D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --OEM��x����ʥ������ �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     C.TEKIYO_STAT = '1'
                --������D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --OEM��x����ʥ������ �ڰ��� �폜�׸� = 0
        AND     C.SAKJO_FLG = '0'
                --OEM��x����ʥ������ �ڰ��݁D�x����� = �x����ʁD�x�����
        AND     C.HARAI = D.HARAI
                --�x����ʁD���������׸� = 0(����ڽ�ΏۊO)
        AND     D.KYOSEI_MUKO_FLG = '0'
        );

-- �XBILL�t���O���(25�Q)���Z�b�g(�x����ʂ�����ڽ�ΏۊO)

UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --�XBILL�t���O_25�Q
        A.YBILL_FLG_25G = ' '
WHERE   EXISTS
        (
        SELECT  1
        FROM    --������,OEM��x����ʥ������ �ڰ���,�x�����
                TB_SEIQ B, TB_OEM_HARAI_SEIQ_R C, TB_HARAI D
        WHERE   --AOBA�]���X��وꎞTBL�D�XBILL�t���O_25�Q = 7
                A.YBILL_FLG_25G = '7'
                --AOBA�]���X��وꎞTBL�D�ڋq���� = ������D�ڋq����
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --OEM��x����ʥ������ �ڰ��݁D�ڋq���� = ������D�ڋq����
        AND     C.KKYK_CD = B.KKYK_CD
                --OEM��x����ʥ������ �ڰ��݁DOEM���� = ������DOEM����
        AND     C.OEM_CD = B.OEM_CD
                --������D�K�p�ð�� = 1(�^�p��)
        AND     B.TEKIYO_STAT = '1'
                --OEM��x����ʥ������ �ڰ��݁D�K�p�ð�� = 1(�^�p��)
        AND     C.TEKIYO_STAT = '1'
                --������D�폜�׸� = 0(���폜)
        AND     B.SAKJO_FLG = '0'
                --OEM��x����ʥ������ �ڰ��� �폜�׸� = 0
        AND     C.SAKJO_FLG = '0'
                --OEM��x����ʥ������ �ڰ��݁D�x����� = �x����ʁD�x�����
        AND     C.HARAI = D.HARAI
                --�x����ʁD���������׸� = 0(����ڽ�ΏۊO)
        AND     D.KYOSEI_MUKO_FLG = '0'
        );


COMMIT;

EXCEPTION
    WHEN OTHERS THEN
    ROLLBACK;
    RAISE_APPLICATION_ERROR(-20500, '[���[�U�G���[]AOBA�]���Q��(AOBA�]���X�r���ꎞ�e�[�u���}��)�ŃG���[���������܂����B');

END;
 
/
