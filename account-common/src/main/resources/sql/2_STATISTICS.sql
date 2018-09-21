#OLE区块入账总额
SELECT SUM(TX_VALUE) FROM TRANSACTION_ETH WHERE COIN_TYPE='OLE' AND TX_TYPE='0' AND TX_STATUS='CZCGYTZ';
#OLE区块入账不包含归集账户
SELECT SUM(TX_VALUE) FROM TRANSACTION_ETH WHERE COIN_TYPE='OLE' AND TX_TYPE='0' AND TX_STATUS='CZCGYTZ' AND TX_TO != '0x55c8b10603bd279edac0d41928457b23a20548cc';
#OLE账户入账总金额
SELECT SUM(TOTAL_IN) FROM ACCOUNT WHERE COIN_TYPE='OLE';

#OLE流水入账总金额
SELECT SUM(BALANCE) FROM ACCOUNT_JNL WHERE COIN_TYPE='OLE' AND TYPE='00';


#ETH区块入账总额
SELECT SUM(TX_VALUE) FROM TRANSACTION_ETH WHERE COIN_TYPE='ETH' AND TX_TYPE='0' AND TX_STATUS='CZCGYTZ';
#ETH区块入账不包含归集账户
SELECT SUM(TX_VALUE) FROM TRANSACTION_ETH WHERE COIN_TYPE='ETH' AND TX_TYPE='0' AND TX_STATUS='CZCGYTZ' AND TX_TO != '0x55c8b10603bd279edac0d41928457b23a20548cc';

#ETH账户入账总金额
SELECT SUM(TOTAL_IN) FROM ACCOUNT WHERE COIN_TYPE='ETH';

#ETH流水入账总金额
SELECT SUM(BALANCE) FROM ACCOUNT_JNL WHERE COIN_TYPE='ETH' AND TYPE='00';


#BTC区块入账总额
SELECT SUM(TX_VALUE) FROM TRANSACTION_BTC_UTXO WHERE TX_TYPE='0' AND TX_STATUS='CZCGYTZ';
#BTC区块入账不包含归集账户
SELECT SUM(TX_VALUE) FROM TRANSACTION_BTC_UTXO WHERE TX_TYPE='0' AND TX_STATUS='CZCGYTZ' AND TX_TO != '1D9zfFXgzzy6bsDbBxGVbuycY2eM2q8DQk';

#BTC账户入账总金额
SELECT SUM(TOTAL_IN) FROM ACCOUNT WHERE COIN_TYPE='BTC';

#BTC流水入账总金额
SELECT SUM(BALANCE) FROM ACCOUNT_JNL WHERE COIN_TYPE='BTC' AND TYPE='00';



#OLE区块出账总额
SELECT SUM(TX_VALUE) FROM TRANSACTION_ETH WHERE COIN_TYPE='OLE' AND TX_TYPE='1' AND TX_STATUS='QXCGYTZ';

#OLE账户出账总金额
SELECT SUM(TOTAL_OUT) FROM ACCOUNT WHERE COIN_TYPE='OLE';

#OLE流水出账总金额
SELECT SUM(BALANCE) FROM ACCOUNT_JNL WHERE COIN_TYPE='OLE' AND TYPE='01';

#OLE提现总金额,ETH书续费
SELECT SUM(AMOUNT),SUM(FEE) FROM WITHDRAWAL WHERE SUB_COIN_TYPE='OLE';
#OLE提现总金额,ETH书续费,只包含归集账户
SELECT SUM(AMOUNT),SUM(FEE) FROM WITHDRAWAL WHERE SUB_COIN_TYPE='OLE' AND TX_FROM = '0x55c8b10603bd279edac0d41928457b23a20548cc';

#ETH区块出账总额
SELECT SUM(TX_VALUE)+SUM(ACTUAL_FEE) FROM TRANSACTION_ETH WHERE COIN_TYPE='ETH' AND TX_TYPE='1' AND TX_STATUS='QXCGYTZ';
SELECT SUM(ACTUAL_FEE) FROM TRANSACTION_ETH WHERE COIN_TYPE='OLE' AND TX_TYPE='1' AND TX_STATUS='QXCGYTZ';

#ETH账户出账总金额
SELECT SUM(TOTAL_OUT) FROM ACCOUNT WHERE COIN_TYPE='ETH';

#ETH流水出账总金额
SELECT SUM(BALANCE) FROM ACCOUNT_JNL WHERE COIN_TYPE='ETH' AND TYPE='01';

#ETH、OLE提现全金额
SELECT SUM(AMOUNT)+SUM(FEE) FROM WITHDRAWAL WHERE COIN_TYPE='ETH' AND SUB_COIN_TYPE IS NULL;
SELECT SUM(FEE) FROM WITHDRAWAL WHERE SUB_COIN_TYPE='OLE';

#ETH提现全金额、提现总金额、手续费
SELECT SUM(AMOUNT)+SUM(FEE),SUM(AMOUNT),SUM(FEE) FROM WITHDRAWAL WHERE COIN_TYPE='ETH' AND SUB_COIN_TYPE IS NULL;
#ETH提现总金额,ETH书续费,只包含归集账户
SELECT SUM(AMOUNT),SUM(FEE) FROM WITHDRAWAL WHERE COIN_TYPE='ETH' AND SUB_COIN_TYPE IS NULL AND TX_FROM = '0x55c8b10603bd279edac0d41928457b23a20548cc';


#BTC区块出账总额
SELECT SUM(TX_VALUE) FROM TRANSACTION_BTC_UTXO WHERE TX_TYPE='1' AND TX_STATUS='QXCGYTZ';

#BTC账户出账总金额
SELECT SUM(TOTAL_OUT) FROM ACCOUNT WHERE COIN_TYPE='BTC';

#BTC流水出账总金额
SELECT SUM(BALANCE) FROM ACCOUNT_JNL WHERE COIN_TYPE='BTC' AND TYPE='01';

#BTC提现全金额、提现总金额、手续费
SELECT SUM(AMOUNT)+SUM(FEE),SUM(AMOUNT),SUM(FEE) FROM WITHDRAWAL WHERE COIN_TYPE='BTC';
#BTC提现总金额,只包含归集账户
SELECT SUM(AMOUNT),SUM(FEE) FROM WITHDRAWAL WHERE COIN_TYPE='BTC' AND TX_FROM = '1D9zfFXgzzy6bsDbBxGVbuycY2eM2q8DQk';


#OLE账户入账总额、出账总额、余额、冻结金额，账户入账总额-出账总额-余额-冻结金额
SELECT SUM(TOTAL_IN),SUM(TOTAL_OUT),SUM(BALANCE),SUM(FROZEN_AMOUNT),SUM(TOTAL_IN)-SUM(TOTAL_OUT)-SUM(BALANCE)-SUM(FROZEN_AMOUNT) FROM ACCOUNT WHERE COIN_TYPE='OLE';

#ETH账户入账总额、出账总额、余额、冻结金额，账户入账总额-出账总额-余额-冻结金额
SELECT SUM(TOTAL_IN),SUM(TOTAL_OUT),SUM(BALANCE),SUM(FROZEN_AMOUNT),SUM(TOTAL_IN)-SUM(TOTAL_OUT)-SUM(BALANCE)-SUM(FROZEN_AMOUNT) FROM ACCOUNT WHERE COIN_TYPE='ETH';

#BTC账户入账总额、出账总额、余额、冻结金额，账户入账总额-出账总额-余额-冻结金额
SELECT SUM(TOTAL_IN),SUM(TOTAL_OUT),SUM(BALANCE),SUM(FROZEN_AMOUNT),SUM(TOTAL_IN)-SUM(TOTAL_OUT)-SUM(BALANCE)-SUM(FROZEN_AMOUNT) FROM ACCOUNT WHERE COIN_TYPE='BTC';
