//
//  JiuCuoViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "JiuCuoViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
@interface JiuCuoViewController ()
{
    NSMutableArray *aryPhoto;
}
@end

@implementation JiuCuoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
   // [self getDataFromServer];
    [self setUI];
    
    
}

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

-(void)setUI
{
    int kind = [self.data[@"kind"] intValue];
    if(kind == 1)
    {
        [self.btnKuiDa setSelected:YES];
        [self.btnHuJia setSelected:NO];
    }else if(kind == 2)
    {
        [self.btnKuiDa setSelected:NO];
        [self.btnHuJia setSelected:YES];
    }
    int status = [self.data[@"status"] intValue];
    if(status == 0)
       [self.imgAuth setImage:nil];
    else if(status == 1)
        [self.imgAuth setImage:[UIImage imageNamed:@"label_shenhezhong.png"]];
    else if(status == 2)
        [self.imgAuth setImage:[UIImage imageNamed:@"label_yirenzheng.png"]];
    else if(status == 3)
        [self.imgAuth setImage:[UIImage imageNamed:@"label_yijujue.png"]];
    self.imgAuth.layer.zPosition = 1000;
    
    self.lblReason.text = self.data[@"reason"];
    self.lblWhy.text = self.data[@"whyis"];
    self.lblEstimaterName.text = self.data[@"estimaterName"];
    self.lblEstimaterComment.text = self.data[@"estimateContent"];
    int akind = [self.data[@"estimaterAkind"] intValue];
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, self.data[@"estimaterLogo"] ]];
    [self.imgEstimaterPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:akind == 1 ? @"no_image_person.png" : @"no_image_enter.png"]];

    
    if(akind == 1)
        [self.imgBusinessType setImage:[UIImage imageNamed:@"personal"]];
    else if(akind == 2)
        [self.imgBusinessType setImage:[UIImage imageNamed:@"office"]];
    self.lblTime.text = [GeneralUtil getDateHourMinFrom:self.data[@"writeTimeString"]];
    
    aryPhoto = self.data[@"estimateImgPaths"];
    for (int i = 0; i < aryPhoto.count; i++)
    {
        url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPhoto[i] ]];
        UIImageView *imgView = [[UIImageView alloc] init];
        [imgView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        [imgView setFrame:CGRectMake(i * 90, 0, 80, 80)];
        [self.scrollResonView addSubview:imgView];
    }
    
    [self.scrollResonView setContentSize:CGSizeMake(aryPhoto.count * 90 - 10, 80)];
    
    aryPhoto = self.data[@"imgPaths"];
    for (int i = 0; i < aryPhoto.count; i++)
    {
        url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPhoto[i] ]];
        UIImageView *imgView = [[UIImageView alloc] init];
        [imgView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        [imgView setFrame:CGRectMake(i * 90, 0, 80, 80)];
        [self.scrollPersonView addSubview:imgView];
    }
    
    [self.scrollPersonView setContentSize:CGSizeMake(aryPhoto.count * 90 - 10, 80)];
}
- (void)getDataFromServer {
    
    UIImage *img1 = [UIImage imageNamed:@"wo_jilu"];
    UIImage *img2 = [UIImage imageNamed:@"wo_renzheng"];
    UIImage *img3 = [UIImage imageNamed:@"1100"];
    
    aryPhoto = [[NSMutableArray alloc] init];
    [aryPhoto addObject:img1];
    [aryPhoto addObject:img2];
    [aryPhoto addObject:img3];
}

@end
