//
//  ProductDetailsViewController.m
//  chengxin
//
//  Created by common on 7/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ProductDetailsViewController.h"
#import "Global.h"
#import "WebAPI.h"

@interface ProductDetailsViewController ()

@end

@implementation ProductDetailsViewController
@synthesize slideImageView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self setUI];
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
}
-(void)setUI
{
    long nImageCount = [self.productDetail[@"imgPaths"] count];
    slideImageView.contentSize = CGSizeMake(slideImageView.frame.size.width * nImageCount, slideImageView.frame.size.height);
    CGRect ViewSize = slideImageView.bounds;
    int nImageNum = 0;
    while (nImageNum < nImageCount) {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [self.productDetail[@"imgPaths"] objectAtIndex:nImageNum]]];
        [imageView setImage:[UIImage imageWithData:[NSData dataWithContentsOfURL:url]]];
        
        [slideImageView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideImageView.bounds.size.width, 0);
        nImageNum ++;
    }

}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
