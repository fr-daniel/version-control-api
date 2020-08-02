package br.com.atlantico.versioncontrolapi.model;

public enum TipoVersao {

    INICIAL {
        @Override
        public void updateVersion(Firmware firmware) {
            firmware.initVersion();
        }
    },
    CORRECAO_FALHAS {
        @Override
        public void updateVersion(Firmware firmware) {
            firmware.nextPatch();
        }
    },
    ALTERACOES_COMPATIVEIS {
        @Override
        public void updateVersion(Firmware firmware) {
            firmware.nextMinor();
        }
    },
    ALTERACOES_INCOMPATIVEIS {
        @Override
        public void updateVersion(Firmware firmware) {
            firmware.nextMajor();
        }
    };

    public abstract void updateVersion(Firmware firmware);
}
