import { QueristModule } from './querist.module';

describe('QueristModule', () => {
  let queristModule: QueristModule;

  beforeEach(() => {
    queristModule = new QueristModule();
  });

  it('should create an instance', () => {
    expect(queristModule).toBeTruthy();
  });
});
